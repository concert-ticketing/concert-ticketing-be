package ticketing.ticketing.application.service.concert;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageAddThumbNailReadResponse;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse;
import ticketing.ticketing.application.dto.imagesDto.ImagesReadResponse;
import ticketing.ticketing.application.service.images.ImagesService;
import ticketing.ticketing.infrastructure.ConcertRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertService {

    public final ConcertRepository concertRepository;
    public final ImagesService imagesService;

    public List<ConcertMainPageAddThumbNailReadResponse> getMainPageSearchConcert(int size, Long lastId) {
        Pageable pageable = PageRequest.of(0, size);
        if(lastId == null){
            List<ConcertMainPageInformationReadResponse> concertList = concertRepository.getConcertSearchBySize(pageable);
            List<ImagesReadResponse> getConcertImagesList = getConcertImagesInfo(concertList);

            Map<Long, String> concertImagesMap = getConcertImagesList.stream()
                    .collect(Collectors.toMap(
                            ImagesReadResponse::getConcertId,
                            ImagesReadResponse::getThumbNailImageUrl
                    ));
            return concertList.stream()
                    .map(c -> new ConcertMainPageAddThumbNailReadResponse(
                            c.getId(),
                            c.getTitle(),
                            c.getStartDate(),
                            c.getEndDate(),
                            concertImagesMap.getOrDefault(c.getId(),null)
                    )).collect(Collectors.toList());

        }
        else{
            //return concertRepository.getConcertSearchBySizeAndLastId(pageable,lastId);
            return null;
        }
    }

    //콘서트 키 값을 이용한 이미지 parsing
    public List<ImagesReadResponse> getConcertImagesInfo(List<ConcertMainPageInformationReadResponse> concertInfo) {

        List<Long> concertIds = concertInfo.stream()
                .map(ConcertMainPageInformationReadResponse::getId)
                .collect(Collectors.toList());

        return imagesService.getThumbNailImagesList(concertIds);
    }
}
