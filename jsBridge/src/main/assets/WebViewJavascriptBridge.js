//notation: js file can only use this kind of comments
//since comments will cause error when use in webview.loadurl,
//comments will be remove by java use regexp
(function() {
    console.log("loading js bridge");
    if (window.WebViewJavascriptBridge) {
        return;
    }

    var token = Date.now();
    deliApp.onLoadNewBridge(token);

    //只是在还没完成初始化的时候使用
    var receiveMessageQueue = [];
    var messageHandlers = {};

    var callbackMaps = {};
    var uniqueId = 1;


    //set default messageHandler  初始化默认的消息线程
    function init(messageHandler) {
        if (WebViewJavascriptBridge._messageHandler) {
            throw new Error('WebViewJavascriptBridge.init called twice');
        }
        WebViewJavascriptBridge._messageHandler = messageHandler;
        var receivedMessages = receiveMessageQueue;
        receiveMessageQueue = null;
        for (var i = 0; i < receivedMessages.length; i++) {
            _dispatchMessageFromNative(receivedMessages[i]);
        }
        console.log("webview: init called");
    }

    // 发送
    function send(data, responseCallback) {
        _doSend('send', data, responseCallback);
    }

    // 注册线程 往数组里面添加值
    function registerHandler(handlerName, handler) {
        messageHandlers[handlerName] = handler;
    }

    var callHandlerPromise = null;

        if(typeof Promise !== "undefined" && Promise.toString().indexOf("[native code]") !== -1){
            callHandlerPromise = function(handlerName, data){
                return new Promise(function(resolve, reject){
                    if (arguments.length == 2 && typeof data == 'function') {
                    			responseCallback = data;
                    			data = null;
                    }
                    _doSend(handlerName, data, resolve);
                });
            }
        }

    // 调用原生方法
    // 调用线程
    function callHandler(handlerName, data, responseCallback) {
        // 如果方法不需要参数，只有回调函数，简化JS中的调用
        if (arguments.length == 2 && typeof data == 'function') {
			responseCallback = data;
			data = null;
		}
        _doSend(handlerName, data, responseCallback);
    }

    //sendMessage add message, 触发native处理 sendMessage
    function _doSend(handlerName, message, responseCallback) {
        var callbackId;
        if(typeof responseCallback === 'string'){
            callbackId = responseCallback;
        } else if (responseCallback) {
            callbackId = 'cb_' + (uniqueId++) + '_' + new Date().getTime();
            callbackMaps[callbackId] = responseCallback;
        }else{
            callbackId = '';
        }
        try {
             // eval(str), 把str作为js代码执行，webview 通过 addJavascriptInterface 注入了java方法
             // 这句是获得方法（方法指针），不是去调用
             console.log(handlerName);
             var fn = eval('window.deliApp.' + handlerName);
         } catch(e) {
             console.log('catch error:');
             console.log(e);
         }
         if (typeof fn === 'function'){
             // 真正调用native 方法的地方
//             var responseData = fn.call(this, JSON.stringify(message), callbackId);
//             var responseData = eval('window.deliApp.' + handlerName + '(' + JSON.stringify(message) +', ' + callbackId + ))
             var responseData = fn.call(window.deliApp, JSON.stringify(message), callbackId);
             // 直接用 native的return值，同步调用， 但ios的原生与h5交互方案不支持同步。。。但android端记得返回，不要void

//             console.log('response message: '+ responseData);
             responseCallback = callbackMaps[callbackId];
//             if(responseData){
//                responseCallback(responseData);
//             }else{
//                responseCallback();
//             }
//             delete callbackMaps[callbackId];
             if(responseData){
              console.log('response message: '+ responseData);
                 responseCallback = callbackMaps[callbackId];
                 if (!responseCallback) {
                     return;
                  }
                 responseCallback(responseData);
                 delete callbackMaps[callbackId];
             }
         }
    }

    //提供给native使用,
    function _dispatchMessageFromNative(messageJSON) {
        setTimeout(function() {
            var message = JSON.parse(messageJSON);
            var responseCallback;
            // java call finished, now need to call js callback function
            // 但现在android是同步返回的，所以不会进入这里的代码，而是进入 _doSend的 responseData那里（看_doSend注释）
            if (message.responseId) {
                responseCallback = callbackMaps[message.responseId];
                if (!responseCallback) {
                    return;
                }
                responseCallback(message.responseData);
                delete callbackMaps[message.responseId];
            } else {
                //直接发送
                if (message.callbackId) {
                    var callbackResponseId = message.callbackId;
                    responseCallback = function(responseData) {
                        _doSend('response', responseData, callbackResponseId);
                    };
                }

                var handler = WebViewJavascriptBridge._messageHandler;
                if (message.handlerName) {
                    handler = messageHandlers[message.handlerName];
                }
                //查找指定handler
                try {
                    handler(message.data, responseCallback);
                } catch (exception) {
                    if (typeof console != 'undefined') {
                        console.log("WebViewJavascriptBridge: WARNING: javascript handler threw.", message, exception);
                    }
                }
            }
        });
    }

    // 原生调用js的入口
    // 提供给native调用,receiveMessageQueue 在会在页面加载完后赋值为null,所以
    function _handleMessageFromNative(messageJSON) {
        console.log('handle message: '+ messageJSON);
        if (receiveMessageQueue) {
            receiveMessageQueue.push(messageJSON);
        }
        _dispatchMessageFromNative(messageJSON);

    }

    //kl add this,to identify a WebViewJavascriptBridge
    function getToken(){
        return token;
    }

    function addEventListener(eventName, handler){
            if(arguments.length == 2){
                eventName = arguments[0];
                handler = arguments[1];
            }else if(arguments.length == 3){
                eventName = arguments[0] + "." + arguments[1];
                handler = arguments[2];
            }
            deliApp.registerEventListener(eventName);
            WebViewJavascriptBridge.registerHandler(eventName, handler);
    }

    function removeListenEvent(eventName){
        deliApp.unRegisterEventListener(eventName);
        delete messageHandlers[eventName];
    }

    var WebViewJavascriptBridge = window.WebViewJavascriptBridge = {
        init: init,
        send: send,
        registerHandler: registerHandler,
        callHandler: callHandler,
        _handleMessageFromNative: _handleMessageFromNative,
        getToken: getToken,
        addEventListener: addEventListener,
        removeListenEvent: removeListenEvent
    };

    var doc = document;
    var readyEvent = doc.createEvent('Events');
    var jobs = window.WVJBCallbacks || [];
    readyEvent.initEvent('WebViewJavascriptBridgeReady');
    console.log("WebViewJavascriptBridgeReady kl");
    readyEvent.bridge = WebViewJavascriptBridge;
    window.WVJBCallbacks = [];
    jobs.forEach(function (job) {
        job(WebViewJavascriptBridge)
    });

    if(callHandlerPromise != null){
        WebViewJavascriptBridge.callHandlerPromise = callHandlerPromise;
    }
    doc.dispatchEvent(readyEvent);
    WebViewJavascriptBridge.initfinished = {};

    deliApp.triggerEvent(JSON.stringify({name:"onJsBridgeLoaded", params: String(token)}));
    console.log('WebViewJavascriptBridge.js load finish');
})();
