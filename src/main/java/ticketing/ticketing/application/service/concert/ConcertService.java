package ticketing.ticketing.application.service.concert;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ticketing.ticketing.application.dto.concertDto.ConcertDetailPageReadResponse;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageAddThumbNailReadResponse;
import ticketing.ticketing.application.dto.concertDto.ConcertMainPageInformationReadResponse;
import ticketing.ticketing.application.dto.imagesDto.ImagesReadResponse;
import ticketing.ticketing.application.dto.imagesDto.ImagesThumbNailReadResponse;
import ticketing.ticketing.application.service.cast.CastService;
import ticketing.ticketing.application.service.images.ImagesService;
import ticketing.ticketing.infrastructure.repository.concert.ConcertRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertService {

    public final ConcertRepository concertRepository;
    public final ImagesService imagesService;
    public final CastService castService;

    public List<ConcertMainPageAddThumbNailReadResponse> getMainPageSearchConcert(int size) {
        Pageable pageable = PageRequest.of(0, size);
        List<ConcertMainPageInformationReadResponse> concertList = concertRepository.getConcertSearchBySize(pageable);
        return concertMainPageAddThumbNail(concertList);
    }

    public List<ConcertMainPageAddThumbNailReadResponse> getMainPageSearchConcertAddLastId(int size, Long lastId) {
        Pageable pageable = PageRequest.of(0, size);
        List<ConcertMainPageInformationReadResponse> concertList = concertRepository.getConcertSearchBySizeAndLastId(pageable, lastId);
        return concertMainPageAddThumbNail(concertList);
    }

    public List<ConcertMainPageAddThumbNailReadResponse> getHighRatingConcertList(int size) {
        Pageable pageable = PageRequest.of(0, size);
        List<ConcertMainPageInformationReadResponse> concertList = concertRepository.getHighRatingConcertSearchBySize(pageable);
        return concertMainPageAddThumbNail(concertList);
    }

    public List<ConcertMainPageAddThumbNailReadResponse> getHighRatingConcertListAddLastId(int size, Long lastId) {
        Pageable pageable = PageRequest.of(0, size);
        List<ConcertMainPageInformationReadResponse> concertList = concertRepository.getHighRatingConcertSearchBySizeAndLastId(pageable, lastId);
        return concertMainPageAddThumbNail(concertList);
    }

    //콘서트 키 값을 이용한 이미지 parsing
    public List<ImagesThumbNailReadResponse> getConcertImagesInfo(List<ConcertMainPageInformationReadResponse> concertInfo) {

        List<Long> concertIds = concertInfo.stream()
                .map(ConcertMainPageInformationReadResponse::getId)
                .collect(Collectors.toList());

        return imagesService.getThumbNailImagesList(concertIds);
    }

    //콘서트 조회 후 Mapping 하는 기능
    public List<ConcertMainPageAddThumbNailReadResponse> concertMainPageAddThumbNail(List<ConcertMainPageInformationReadResponse> concertList) {
        List<ImagesThumbNailReadResponse> concertImagesList = getConcertImagesInfo(concertList);
        Map<Long, String> concertImagesMap = concertImagesList.stream()
                .collect(Collectors.toMap(
                        ImagesThumbNailReadResponse::getConcertId,
                        ImagesThumbNailReadResponse::getThumbNailImageUrl
                ));
        return concertList.stream()
                .map(c -> new ConcertMainPageAddThumbNailReadResponse(
                        c.getId(),
                        c.getTitle(),
                        c.getStartDate(),
                        c.getEndDate(),
                        c.getLocation(),
                        c.getRating(),
                        concertImagesMap.getOrDefault(c.getId(), null)
                )).collect(Collectors.toList());
    }

    public ConcertDetailPageReadResponse getConcertDetailPageById(Long id) {
        List<ImagesReadResponse> getImagesByConcertId = imagesService.getImagesList(id);
        ConcertDetailPageReadResponse getConcertDetailPage = concertRepository.getConcertById(id);
        if (getConcertDetailPage == null) {
            return null;
        }
        getConcertDetailPage.setImages(getImagesByConcertId);
        return getConcertDetailPage;
    }
}
