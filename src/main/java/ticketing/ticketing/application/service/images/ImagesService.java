package ticketing.ticketing.application.service.images;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.imagesDto.ImagesReadResponse;
import ticketing.ticketing.application.dto.imagesDto.ImagesThumbNailReadResponse;
import ticketing.ticketing.infrastructure.repository.image.ImagesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {
    private final ImagesRepository imagesRepository;

    public List<ImagesThumbNailReadResponse> getThumbNailImagesList (List<Long> concertId){
        return imagesRepository.findByThumbailImagesBeforeList(concertId);
    }

    public List<ImagesReadResponse> getImagesList (Long concertId){
        return imagesRepository.findByConcertId(concertId);
    }
}
