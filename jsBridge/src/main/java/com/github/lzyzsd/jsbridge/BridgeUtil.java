package com.github.lzyzsd.jsbridge;

import android.content.Context;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// TODO: 实际上也只剩下 assetFile2Str有用了, 看看什么时候移出来吧
class BridgeUtil {

	public static final String JAVA_SCRIPT = "WebViewJavascriptBridge.js";
	public final static String UNDERLINE_STR = "_";
	public final static String CALLBACK_ID_FORMAT = "JAVA_CB_%s";
	public final static String JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
	public final static String JAVASCRIPT_STR = "javascript:%s";

//	final static String JS_FETCH_QUEUE_FROM_JAVA = "javascript:WebViewJavascriptBridge._fetchQueue();";
//	final static String YY_OVERRIDE_SCHEMA = "yy://";
//	final static String YY_RETURN_DATA = YY_OVERRIDE_SCHEMA + "return/";//格式为   yy://return/{function}/returncontent
//	final static String YY_FETCH_QUEUE = YY_RETURN_DATA + "_fetchQueue/";
//	final static String EMPTY_STR = "";
//	final static String SPLIT_MARK = "/";

//	/**
//	 * js 文件将注入为第一个script引用
//	 * @param view WebView
//	 * @param url url
//	 */
//	public static void webViewLoadJs(WebView view, String url){
//		String js = "var newscript = document.createElement(\"script\");";
//		js += "newscript.src=\"" + url + "\";";
//		js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);";
//		view.loadUrl("javascript:" + js);
//	}

	/**
	 * 这里只是加载lib包中assets中的 WebViewJavascriptBridge.js
	 *
	 * @param view webview
	 * @param path 路径
	 */
	public static boolean webViewLoadLocalJs(WebView view, String path) {
		String jsContent = assetFile2Str(view.getContext(), path);
		if (jsContent == null || jsContent.isEmpty()) {
			return false;
		}
//        view.loadUrl("javascript:" + jsContent);
		view.evaluateJavascript(jsContent, null);
		return true;
	}

//	public static String getFunctionFromReturnUrl(String url) {
//		String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
//		String[] functionAndData = temp.split(SPLIT_MARK);
//		if(functionAndData.length >= 1){
//			return functionAndData[0];
//		}
//		return null;
//	}

//	public static String getDataFromReturnUrl(String url) {
//		if(url.startsWith(YY_FETCH_QUEUE)) {
//			return url.replace(YY_FETCH_QUEUE, EMPTY_STR);
//		}
//
//		String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
//		String[] functionAndData = temp.split(SPLIT_MARK);
//
//		if(functionAndData.length >= 2) {
//			StringBuilder sb = new StringBuilder();
//			for (int i = 1; i < functionAndData.length; i++) {
//				sb.append(functionAndData[i]);
//			}
//			return sb.toString();
//		}
//		return null;
//	}
//
//	public static String parseFunctionName(String jsUrl){
//		return jsUrl.replace("javascript:WebViewJavascriptBridge.", "").replaceAll("\\(.*\\);", "");
//	}

	/**
	 * 解析assets文件夹里面的代码,去除注释,取可执行的代码
	 *
	 * @param c      context
	 * @param urlStr 路径
	 * @return 可执行代码
	 */
	public static String assetFile2Str(Context c, String urlStr) {
		InputStream in = null;
		try {
			in = c.getAssets().open(urlStr);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			StringBuilder sb = new StringBuilder();
			do {
				line = bufferedReader.readLine();
				if (line != null && !line.matches("^\\s*\\/\\/.*")) { // 去除注释， 但，有必要吗？
					sb.append(line);
				}
			} while (line != null);

			bufferedReader.close();
			in.close();

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
