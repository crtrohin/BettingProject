package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.SelectionDTO;
import com.codefactorygroup.betting.service.SelectionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/selections")
public class SelectionController {

    private final SelectionService selectionService;

    public SelectionController(SelectionService selectionService) {
        this.selectionService = selectionService;
    }

    @GetMapping("/{selectionId}")
    public SelectionDTO getSelection(@PathVariable(name = "selectionId") final Integer selectionId) {
        return selectionService.getSelection(selectionId);
    }

    @PostMapping
    public SelectionDTO addSelection(@RequestBody final SelectionDTO selection) {
        return selectionService.addSelection(selection);
    }

    @DeleteMapping("/{selectionId}")
    public void deleteSelection(@PathVariable(name = "selectionId") final Integer selectionId) {
        selectionService.deleteSelection(selectionId);
    }

    @PutMapping("/{selectionId}")
    public SelectionDTO updateSelection(@RequestBody final SelectionDTO selection, @PathVariable(name = "selectionId") final Integer selectionId) {
        return selectionService.updateSelection(selection, selectionId);
    }
}
