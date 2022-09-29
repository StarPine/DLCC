package me.goldze.mvvmhabit.http;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import me.goldze.mvvmhabit.R;
import me.goldze.mvvmhabit.utils.StringUtils;
import retrofit2.HttpException;


/**
 * Created by goldze on 2017/5/11.
 */
public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.message = StringUtils.getString(R.string.error_http_unauthorized);
                    break;
                case FORBIDDEN:
                    ex.message = StringUtils.getString(R.string.error_http_forbidden);
                    break;
                case NOT_FOUND:
                    ex.message = StringUtils.getString(R.string.error_http_not_found);
                    break;
                case REQUEST_TIMEOUT:
                    ex.message = StringUtils.getString(R.string.error_http_request_timeout);
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = StringUtils.getString(R.string.error_http_internal_server_error);
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.message = StringUtils.getString(R.string.error_http_server_unavailable);
                    break;
                default:
                    ex.message = StringUtils.getString(R.string.error_http_network_error);
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            Log.e("该接口解析错误",e.getClass().toString()+"========="+e.getMessage());
            ex.message = StringUtils.getString(R.string.error_parse_error);
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = StringUtils.getString(R.string.error_network_error);
            return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = StringUtils.getString(R.string.error_ssl_exception);
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = StringUtils.getString(R.string.error_connect_timeout);
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = StringUtils.getString(R.string.error_socket_timeout);
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = StringUtils.getString(R.string.error_unknow_host);
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            ex.message = StringUtils.getString(R.string.error_unknown);
            return ex;
        }
    }


    /**
     * 约定异常 这个具体规则需要与服务端或者领导商讨定义
     */
    class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

}

