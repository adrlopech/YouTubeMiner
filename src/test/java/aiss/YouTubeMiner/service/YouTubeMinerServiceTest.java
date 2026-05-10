package aiss.YouTubeMiner.service;

import aiss.YouTubeMiner.model.youtube.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class YouTubeMinerServiceTest {

    @Autowired
    YouTubeMinerService service;

    @Test
    @DisplayName("Probar que recupera el canal de Google Developers")
    void getChannelTest() {
        // 1. Llamamos al metodo con un ID de canal que sabemos que existe
        YouTubeChannelList channelList = service.getChannel("UC_x5XG1OV2P6uZZ5FSM9Ttw");

        // 2. Comprobaciones de seguridad (Assertions)
        assertNotNull(channelList, "El envoltorio del canal no debería ser nulo");
        assertNotNull(channelList.getItems(), "La lista de items no debería ser nula");
        assertFalse(channelList.getItems().isEmpty(), "Debería traer al menos un canal");

        // 3. Print de control
        System.out.println("Canal recuperado: " + channelList.getItems().get(0).getSnippet().getTitle());
    }

    @Test
    @DisplayName("Probar que recupera vídeos de un canal")
    void getVideosFromChannelTest() {
        // 1. Llamamos al metodo con un canal real
        YouTubeVideoList videoList = service.getVideosFromChannel("UC_x5XG1OV2P6uZZ5FSM9Ttw", 5);

        // 2. Comprobaciones de seguridad
        assertNotNull(videoList, "La respuesta no debería ser nula");
        assertNotNull(videoList.getItems(), "La lista de vídeos no debería ser nula");
        assertFalse(videoList.getItems().isEmpty(), "La lista de vídeos no debería estar vacía");

        // 3. Print de control
        System.out.println("Vídeos encontrados: " + videoList.getItems().size());
        System.out.println("ID del primer vídeo: " + videoList.getItems().get(0).getId().getVideoId());
    }

    @Test
    @DisplayName("Probar que recupera comentarios de un vídeo (Comments)")
    void getCommentsTest() {
        // 1. Sacamos primero un vídeo real del canal
        YouTubeVideoList videoList = service.getVideosFromChannel("UC_x5XG1OV2P6uZZ5FSM9Ttw", 5);
        assertFalse(videoList.getItems().isEmpty(), "No hay vídeos para hacer la prueba");
        String realVideoId = videoList.getItems().get(0).getId().getVideoId();

        // 2. Pedimos los comentarios usando ese ID real
        YouTubeCommentList comments = service.getComments(realVideoId, 5);

        // 3. Comprobaciones
        assertNotNull(comments, "El envoltorio de los comentarios no debería ser nulo");
        System.out.println("Comentarios recuperados para el vídeo: " + realVideoId);
    }

    @Test
    @DisplayName("Probar que recupera subtítulos de un vídeo (Captions)")
    void getCaptionsTest() {
        // 1. Volvemos a usar el ID de un vídeo real
        YouTubeVideoList videoList = service.getVideosFromChannel("UC_x5XG1OV2P6uZZ5FSM9Ttw", 5);
        assertFalse(videoList.getItems().isEmpty(), "No hay vídeos para hacer la prueba");
        String realVideoId = videoList.getItems().get(0).getId().getVideoId();

        // 2. Pedimos los subtítulos
        YouTubeCaptionList captions = service.getCaptions(realVideoId);

        // 3. Comprobaciones
        assertNotNull(captions, "El objeto de subtítulos no debería ser nulo");
        System.out.println("Subtítulos recuperados para el vídeo: " + realVideoId);
    }
}