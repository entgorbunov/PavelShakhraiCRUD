package com.crud.servlets;

import com.crud.dataSource.ConnectionManager;
import com.crud.dto.CreateUserDto;
import com.crud.entity.Role;
import com.crud.exceptions.ServletCrudException;
import com.crud.service.impl.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Logger;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private static final UserService USER_SERVICE = UserService.getInstance();
    private static final Logger LOGGER = Logger.getLogger(RegistrationServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate date = LocalDate.now();
        CreateUserDto userDto = CreateUserDto.builder()
                .login(req.getParameter("login"))
                .name(req.getParameter("name"))
                .registrationDate(date)
                .role("USER")
                .build();

        Long aLong = USER_SERVICE.create(userDto);
        if (aLong == null) {
            LOGGER.severe("User creation failed");
            throw new ServletException("User creation failed");
        }
        resp.sendRedirect("/login");
    }


}
