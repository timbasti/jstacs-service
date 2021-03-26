package de.jstacs.service.data;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class ToolTaskFileResult {
    private List<String> results = new ArrayList<String>();
    private double progress = -1;

    public ToolTaskFileResult(List<String> fileResults) {
        this.results = fileResults;
    }

    public ToolTaskFileResult(double progress) {
        this.progress = progress;
    }
}
