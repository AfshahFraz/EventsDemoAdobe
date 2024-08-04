package com.events.demo.core.services;

import java.io.IOException;

public interface FormService {
    void processForm(String id, String firstName, String lastName, String email) throws IOException;
    //void saveFormResponse(String firstName, String lastName, String email, String apiResponse, ResourceResolver resourceResolver) throws PersistenceException;
}
