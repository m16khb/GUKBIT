package com.gukbit.controller;

import com.gukbit.dto.AcademyDto;
import com.gukbit.service.AcademyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/board")
public class ModalController {

    private final AcademyService academyService;

    public ModalController(AcademyService academyService) {
        this.academyService = academyService;
    }

    @PostMapping("/modal")
    public List<AcademyDto> modalReturn(@RequestParam(value = "SearchValue") String searchValue) {
        List<AcademyDto> academyDtoList = academyService.searchAcademy(searchValue);
        for (AcademyDto academyDto : academyDtoList) {
            System.out.println("academyDto = " + academyDto);
        }
        return academyDtoList;
    }

}
