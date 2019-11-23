package com.pos.ecommerce.server;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.mercadopago.MercadoPago;
import com.mercadopago.core.MPResourceArray;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.datastructures.preference.Payer;
import com.mercadopago.resources.datastructures.preference.PaymentMethods;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "MercadoPagoService", urlPatterns = { "/checkMP"})

public class MercadoPagoService extends HttpServlet implements Servlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,NumberFormatException {
        try {
            MercadoPago.SDK.setClientId("4566043430491150");
        } catch (MPException e) {
            e.printStackTrace();
        }
        try {
            MercadoPago.SDK.setClientSecret("e6oL23oPxlOZEwYXSOKnZKjgqw78zXNi");
        } catch (MPException e) {
            e.printStackTrace();
        }

        try {
            MercadoPago.SDK.setAccessToken("TEST-4566043430491150-112118-7cca72f27f55b398f064000be11cafbd-490028165");
        } catch (MPConfException e) {
            e.printStackTrace();
        }
        String exRef = req.getParameter("external_reference");
        HashMap<String, String> filters = new HashMap<>();
        filters.put("external_reference", "Order_123");
        MPResourceArray payment =null;
        try {
            payment= Payment.search(filters,true);
        } catch (MPException e) {
             e.printStackTrace();
        }
        Payment e = (Payment) payment.resources().get(0);
     /*   if (e.getStatus().equals(Payment.Status.approved)){
            resp.sendRedirect("http://127.0.0.1:8888/Ecommerce.html");
        }else if(e.getStatus().equals(Payment.Status.approved)){
            resp.sendRedirect("http://127.0.0.1:8888/Ecommerce.html");
        }else if(e.getStatus().equals(Payment.Status.approved)){
            resp.sendRedirect("http://127.0.0.1:8888/Ecommerce.html");
        }*/
        if (e.getStatus().equals(Payment.Status.approved)){
            resp.sendRedirect("http://localhost:8888/Ecommerce.html");
        }else {
            resp.sendRedirect("http://localhost:8888/Ecommerce.html");
        }
    }
}