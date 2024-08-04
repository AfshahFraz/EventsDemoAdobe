package com.events.demo.core.servlets;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.events.demo.core.services.FormService;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/eventForm/form")
public class FormServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormServlet.class);

    @Reference
    private FormService formService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        try {
            // Process the form using the service
            formService.processForm(id, firstName, lastName, email);

            // Redirect to the thank you page
            response.sendRedirect(request.getContextPath() + "/content/eventsdemo/us/en/eventRegistration/ThankYou.html");
        } catch (RuntimeException e) {
            LOGGER.error("Exception occurred while processing the form: {}", e.getMessage(), e);
            response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the form. Please try again later.");
        }
    }
}
