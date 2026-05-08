package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.model.videominer.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders; // IMPORT CORRECTO
import org.springframework.http.MediaType;

@Service
public class VideoMinerService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${videominer.base.url:http://localhost:8080}")
    private String videoMinerUrl;

    public Channel postChannel(Channel channel) {
        String url = "http://localhost:8080/videominer/api/channels";

        // 1. Creamos las cabeceras y metemos la API Key
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", "trabajoAiss123");

        // 2. Metemos el canal y las cabeceras en un "paquete" llamado HttpEntity
        HttpEntity<Channel> request = new HttpEntity<>(channel, headers);

        // 3. Hacemos el POST enviando el paquete completo (request)
        return restTemplate.postForObject(url, request, Channel.class);
    }
}