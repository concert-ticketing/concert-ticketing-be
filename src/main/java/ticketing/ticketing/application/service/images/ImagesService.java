package ticketing.ticketing.application.service.images;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse;
import ticketing.ticketing.application.dto.imagesDto.ImagesReadResponse;
import ticketing.ticketing.infrastructure.ImagesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagesService {
    private final ImagesRepository imagesRepository;

    public List<ImagesReadResponse> getThumbNailImagesList (List<Long> concertId){
        return imagesRepository.findByThumbailImagesBeforeList(concertId);
    }
}
