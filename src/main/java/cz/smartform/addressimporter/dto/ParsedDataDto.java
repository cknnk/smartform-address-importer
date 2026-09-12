package cz.smartform.addressimporter.dto;

import cz.smartform.addressimporter.entity.CastObce;
import cz.smartform.addressimporter.entity.Obec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedDataDto {
    private Obec obec;
    private List<CastObce> castiObce;
}