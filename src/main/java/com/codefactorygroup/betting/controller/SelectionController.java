package com.codefactorygroup.betting.controller;

import com.codefactorygroup.betting.dto.SelectionDTO;
import com.codefactorygroup.betting.dto.SelectionResultDTO;
import com.codefactorygroup.betting.service.SelectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SelectionController {

    private final SelectionService selectionService;

    public SelectionController(SelectionService selectionService) {
        this.selectionService = selectionService;
    }

    @GetMapping("/selections/{selectionId}")
    public SelectionDTO getSelection(@PathVariable(name = "selectionId") final Integer selectionId) {
        return selectionService.getSelection(selectionId);
    }

    @GetMapping("/selections")
    public List<SelectionDTO> getAllSelections() {
        return selectionService.getAllSelections();
    }

    @GetMapping("/markets/{marketId}/selections")
    public List<SelectionDTO> getSelectionsByMarketId(@PathVariable(name = "marketId") final Integer marketId) {
        return selectionService.getSelectionsByMarketId(marketId);
    }

    @PostMapping("markets/{marketId}/selections")
    public SelectionDTO addSelection(@PathVariable final Integer marketId, @RequestBody final SelectionDTO selection) {
        return selectionService.addSelection(marketId, selection);
    }

    @PutMapping("events/{eventId}/markets/{marketId}/selections/{selectionId}")
    public SelectionDTO setSelectionResult(
            @PathVariable final Integer eventId,
            @PathVariable final Integer marketId,
            @PathVariable final Integer selectionId,
            @RequestBody final SelectionResultDTO selectionResultDTO) {
        return selectionService.setSelectionResult(selectionId, selectionResultDTO);
    }

    @DeleteMapping("/selections/{selectionId}")
    public void deleteSelection(@PathVariable(name = "selectionId") final Integer selectionId) {
        selectionService.deleteSelection(selectionId);
    }

    @PutMapping("/selections/{selectionId}")
    public SelectionDTO updateSelection(@RequestBody final SelectionDTO selection, @PathVariable(name = "selectionId") final Integer selectionId) {
        return selectionService.updateSelection(selection, selectionId);
    }
}
