package com.zen.cam.detector.infraestructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.zen.capture.commons.domain.models.ICapture;
import com.zen.capture.commons.domain.models.impl.StringCapture;

@ExtendWith(MockitoExtension.class)
public class RemoteCamRepositoryTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RemoteCamRepository remoteCamRepository;

    // Tests will be added here

    @Test
    void testGetCaptureWithState_Success() {
        int index = 1;
        int state = 2;
        StringCapture expectedCapture = new StringCapture();
        expectedCapture.setImage("test_capture_data");
        when(restTemplate.getForObject(anyString(), eq(StringCapture.class))).thenReturn(expectedCapture);

        Optional<ICapture<String>> actualCapture = remoteCamRepository.getCapture(index, state);

        assertTrue(actualCapture.isPresent());
        assertEquals(expectedCapture.getImage(), actualCapture.get().getImage());
        verify(restTemplate).getForObject("http://localhost:8080/capture/" + index + "/" + state, StringCapture.class);
    }

    @Test
    void testGetCaptureWithState_NotFound() {
        int index = 1;
        int state = 2;
        when(restTemplate.getForObject(anyString(), eq(StringCapture.class))).thenReturn(null);

        Optional<ICapture<String>> actualCapture = remoteCamRepository.getCapture(index, state);

        assertFalse(actualCapture.isPresent());
        verify(restTemplate).getForObject("http://localhost:8080/capture/" + index + "/" + state, StringCapture.class);
    }

    @Test
    void testUpdateCapture_Success() {
        int index = 1;
        // postForObject with Void.class response type will return null upon success
        when(restTemplate.postForObject(anyString(), isNull(), eq(Void.class))).thenReturn(null);

        remoteCamRepository.updateCapture(index);

        verify(restTemplate).postForObject("http://localhost:8080/capture/" + index + "/update", null, Void.class);
    }

    @Test
    void testUpdateCapture_Failure() {
        int index = 1;
        // Simulate RestClientException, which is more specific for restTemplate failures
        doThrow(new org.springframework.web.client.RestClientException("Server error")).when(restTemplate).postForObject(anyString(), isNull(), eq(Void.class));

        assertThrows(org.springframework.web.client.RestClientException.class, () -> remoteCamRepository.updateCapture(index));

        verify(restTemplate).postForObject("http://localhost:8080/capture/" + index + "/update", null, Void.class);
    }
}
