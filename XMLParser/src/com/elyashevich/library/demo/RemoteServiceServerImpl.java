package com.elyashevich.library.demo;

import com.elyashevich.library.service.RemoteService;
import com.elyashevich.library.service.impl.RemoteServiceImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteServiceServerImpl {

    private static final String BINDING_NAME = "local/LibraryService";

    public static void main(String... args) {
        final Registry registry;
        try {
            registry = LocateRegistry.createRegistry(4398);
           // System.out.println("Registry created.");
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

        RemoteService service = new RemoteServiceImpl();

        Remote stub = null;
        try {
            stub = UnicastRemoteObject.exportObject(service, 0);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
        try {
            registry.bind(BINDING_NAME, stub);
            System.out.println("Service name bound");
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println(e.getMessage());
        }
        while (true) {
            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}