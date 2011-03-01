package com.melexis;

import java.net.*;
import java.io.*;

public class HttpClient {

    public String get(final String location) throws IOException, MalformedURLException {
        final URL url = new URL(location);
        final URLConnection uc = url.openConnection();
        final BufferedReader r = new BufferedReader(new InputStreamReader(uc.getInputStream()));

        final StringBuilder response = new StringBuilder();
        String out;
        while ((out = r.readLine()) != null) {
            response.append(out);
        }

        return response.toString();
    }
}