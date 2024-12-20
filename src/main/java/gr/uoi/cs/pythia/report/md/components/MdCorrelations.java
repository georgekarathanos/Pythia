package gr.uoi.cs.pythia.report.md.components;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import gr.uoi.cs.pythia.model.Column;
import gr.uoi.cs.pythia.report.md.structures.MdBasicStructures;
import gr.uoi.cs.pythia.report.md.structures.MdTable;

public class MdCorrelations {

    private final List<Column> columns;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.###", 
    		new DecimalFormatSymbols(Locale.ENGLISH));;

    public MdCorrelations(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return getTitle() + "\n" +
                MdBasicStructures.horizontalLine() + "\n" +
                getCorrelationsTable() + "\n";
    }

    private String getTitle() {
        return MdBasicStructures.center(MdBasicStructures.heading2("Correlations"));
    }

    private String getCorrelationsTable() {
        List<String> columnNames = getColumnNames();
        String table = new MdTable(getTableHeaders(columnNames), getTableData(),
                MdTable.ALIGNMENT_TYPE.CENTER)
                .getTable();
        return MdBasicStructures.center(table);
    }

    private List<String> getColumnNames() {
        return columns.stream()
                .map(Column::getName)
                .collect(Collectors.toList());
    }

    private List<String> getTableHeaders(List<String> columnNames) {
        List<String> headers = new ArrayList<>();
        headers.add("Correlations");
        headers.addAll(columnNames);
        return headers;
    }

    private List<List<String>> getTableData() {
        List<List<String>> data = new ArrayList<>();
        for (Column column : columns) {
            data.add(getColumnData(column));
        }
        return data;
    }

    private List<String> getColumnData(Column column) {
        Map<String, Double> correlations = new HashMap<>();
        if (column.getCorrelationsProfile() != null)
            correlations = column.getCorrelationsProfile().getAllCorrelations();

        List<String> data = new ArrayList<>();
        data.add(MdBasicStructures.bold(column.getName()));
        for (Column corrColumn : columns) {
            if (correlations.containsKey(corrColumn.getName()))
                data.add(decimalFormat.format(correlations.get(corrColumn.getName())));
            else
                data.add(null);
        }
        return data;
    }
}
