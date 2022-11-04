package com.codefactorygroup.betting.kafka;

import com.codefactorygroup.betting.dto.SelectionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerSelections {

    @Autowired
    private KafkaTemplate<String, SelectionDTO> kafkaTemplate;

    private final static String TOPIC = "selections";

    public void sendUpdatedSelectionMessage(SelectionDTO message) {
        kafkaTemplate.send(TOPIC, message);
    }
    
}
