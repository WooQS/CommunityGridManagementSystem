package com.wooqs.cgms.controller;

import com.wooqs.cgms.model.Population;
import com.wooqs.cgms.service.PopulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/populations")
public class PopulationController {

    @Autowired
    private PopulationService populationService;

    // 获取所有Population记录
    @GetMapping
    public ResponseEntity<List<Population>> getAllPopulations() {
        List<Population> populations = populationService.getAllPopulations();
        return new ResponseEntity<>(populations, HttpStatus.OK);
    }

    // 根据ID获取一个Population记录
    @GetMapping("/id/{id}")
    public ResponseEntity<Population> getPopulationById(@PathVariable Long id) {
        Population population = populationService.getPopulationById(id);
        return new ResponseEntity<>(population, HttpStatus.OK);
    }

    // 根据社区ID获取同一个社区的Population记录
    @GetMapping("/community/{communityId}")
    public ResponseEntity<List<Population>> getPopulationsByCommunityId(@PathVariable Long communityId) {
        List<Population> populations = populationService.getPopulationByCommunityId(communityId);
        return new ResponseEntity<>(populations, HttpStatus.OK);
    }

    // 根据姓名获取Population记录
    @GetMapping("/{name}&{communityId}")
    public ResponseEntity<List<Population>> search(@PathVariable String name, @PathVariable Long communityId) {
        List<Population> populations = populationService.search(name,communityId);
        return new ResponseEntity<>(populations, HttpStatus.OK);
    }


    // 添加一个新的Population记录
    @PostMapping
    public ResponseEntity<Void> addPopulation(@RequestBody Population population) {
        try{populationService.addPopulation(population);}
        catch (Exception MySQLIntegrityConstraintViolationException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 根据ID更新一个Population记录
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updatePopulation(@PathVariable Long id, @RequestBody Population population) {
        population.setPopulationId(id);
        try{populationService.updatePopulation(population);}
        catch (Exception MySQLIntegrityConstraintViolationException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 根据ID删除一个Population记录
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePopulationById(@PathVariable Long id) {
        populationService.deletePopulationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
