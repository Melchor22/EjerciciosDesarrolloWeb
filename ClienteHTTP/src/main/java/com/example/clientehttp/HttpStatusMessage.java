package com.example.clientehttp;

import java.util.HashMap;
import java.util.Map;

public class HttpStatusMessage {
    private static final Map<Integer, String> statusMessages = new HashMap<>();

    static {
        statusMessages.put(100, "Continue");
        statusMessages.put(101, "Switching Protocols");
        statusMessages.put(102, "Processing");
        statusMessages.put(103, "Early Hints");
        statusMessages.put(200, "OK");
        statusMessages.put(201, "Created");
        statusMessages.put(202, "Accepted");
        statusMessages.put(203, "Non-Authoritative Information");
        statusMessages.put(204, "No Content");
        statusMessages.put(205, "Reset Content");
        statusMessages.put(206, "Partial Content");
        statusMessages.put(207, "Multi-Status");
        statusMessages.put(208, "Already Reported");
        statusMessages.put(226, "IM Used");
        statusMessages.put(300, "Multiple Choices");
        statusMessages.put(301, "Moved Permanently");
        statusMessages.put(302, "Found");
        statusMessages.put(303, "See Other");
        statusMessages.put(304, "Not Modified");
        statusMessages.put(305, "Use Proxy");
        statusMessages.put(307, "Temporary Redirect");
        statusMessages.put(308, "Permanent Redirect");
        statusMessages.put(400, "Bad Request");
        statusMessages.put(401, "Unauthorized");
        statusMessages.put(402, "Payment Required");
        statusMessages.put(403, "Forbidden");
        statusMessages.put(404, "Not Found");
        statusMessages.put(405, "Method Not Allowed");
        statusMessages.put(406, "Not Acceptable");
        statusMessages.put(407, "Proxy Authentication Required");
        statusMessages.put(408, "Request Timeout");
        statusMessages.put(409, "Conflict");
        statusMessages.put(410, "Gone");
        statusMessages.put(411, "Length Required");
        statusMessages.put(412, "Precondition Failed");
        statusMessages.put(413, "Payload Too Large");
        statusMessages.put(414, "URI Too Long");
        statusMessages.put(415, "Unsupported Media Type");
        statusMessages.put(416, "Range Not Satisfiable");
        statusMessages.put(417, "Expectation Failed");
        statusMessages.put(418, "I'm a teapot");
        statusMessages.put(421, "Misdirected Request");
        statusMessages.put(422, "Unprocessable Entity");
        statusMessages.put(423, "Locked");
        statusMessages.put(424, "Failed Dependency");
        statusMessages.put(425, "Too Early");
        statusMessages.put(426, "Upgrade Required");
        statusMessages.put(428, "Precondition Required");
        statusMessages.put(429, "Too Many Requests");
        statusMessages.put(431, "Request Header Fields Too Large");
        statusMessages.put(451, "Unavailable For Legal Reasons");
        statusMessages.put(500, "Internal Server Error");
        statusMessages.put(501, "Not Implemented");
        statusMessages.put(502, "Bad Gateway");
        statusMessages.put(503, "Service Unavailable");
        statusMessages.put(504, "Gateway Timeout");
        statusMessages.put(505, "HTTP Version Not Supported");
        statusMessages.put(506, "Variant Also Negotiates");
        statusMessages.put(507, "Insufficient Storage");
        statusMessages.put(508, "Loop Detected");
        statusMessages.put(510, "Not Extended");
        statusMessages.put(511, "Network Authentication Required");
    }

    public static String getMessage(int statusCode) {
        return statusMessages.getOrDefault(statusCode, "Unknown");
    }
}
