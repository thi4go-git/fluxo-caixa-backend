package com.dynss.cloudtecnologia.rest.dto.metabase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetabaseEmbedResponseDTO {
    private String token;
    private String instanceUrl;
}
