package com.bus_reservation_system.controller;

import com.bus_reservation_system.exception.ResourceNotFoundException;
import com.bus_reservation_system.model.Bus;
import com.bus_reservation_system.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/buses")
public class BusController {

    @Autowired
    private BusService busService;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bus", new Bus());
        return "bus/bus-form";
    }

    @PostMapping("/add")
    public String addBus(@ModelAttribute Bus bus) {
        busService.saveBus(bus);
        return "redirect:/buses/list";
    }

    @GetMapping("/list")
    public String viewAllBuses(Model model) {
        model.addAttribute("buses", busService.getAllBuses());
        return "bus/bus-list";
    }

    @GetMapping("/detail/{id}")
    public String ShowBusDetail(@PathVariable String id, Model model){
        Bus bus=busService.getBusById(id).orElseThrow(
                ()->new ResourceNotFoundException("Bus not found")
        );
        model.addAttribute("bus",bus);
        return "bus/bus-detail";
    }

    @GetMapping("/update/{id}")
    public String showupdateBusform(@PathVariable String id,Model model){
        Bus bus = busService.getBusById(id).orElseThrow(
                ()->new ResourceNotFoundException("Bus not found")
        );
        model.addAttribute("bus",bus);
        return "bus/bus-update";
    }

    @PostMapping("/update/{id}")
    public String updateBusbyId(@PathVariable String id,@ModelAttribute Bus bus){
        busService.updateBusbyId(id, bus);
        return "redirect:/buses/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBusbyId(@PathVariable String id){
        busService.deleteBusById(id);
        return "redirect:/buses/list";
    }
}
