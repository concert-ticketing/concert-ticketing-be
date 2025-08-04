package ticketing.ticketing.application.service.concertHall;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.ticketing.application.dto.concertHallAreaRequestDto.ConcertHallAreaRequestDto;
import ticketing.ticketing.application.dto.concertHallCreateRequestDto.ConcertHallCreateRequestDto;
import ticketing.ticketing.application.dto.concertHallResponseDto.ConcertHallResponseDto;
import ticketing.ticketing.application.dto.seatResponseDto.SeatResponseDto;
import ticketing.ticketing.infrastructure.repository.admin.AdminRepository;
import ticketing.ticketing.infrastructure.repository.consertHallArea.ConcertHallAreaRepository;
import ticketing.ticketing.infrastructure.repository.consertHall.ConcertHallRepository;
import ticketing.ticketing.infrastructure.repository.seat.SeatsRepository;
import ticketing.ticketing.domain.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertHallService {

    private final ConcertHallRepository concertHallRepository;
    private final ConcertHallAreaRepository concertHallAreaRepository;
    private final SeatsRepository seatsRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public void createConcertHall(ConcertHallCreateRequestDto dto) {
        Admin admin = adminRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));

        ConcertHall concertHall = ConcertHall.create(dto.getConcertHallName(), admin);
        concertHallRepository.save(concertHall);

        for (ticketing.ticketing.application.dto.concertHallAreaRequestDto.ConcertHallAreaRequestDto areaDto : dto.getAreas()) {
            ConcertHallArea area = ConcertHallArea.create(
                    areaDto.getAreaName(),
                    concertHall,
                    areaDto.getX(),
                    areaDto.getY(),
                    areaDto.getUiMetadata()
            );
            concertHallAreaRepository.save(area);

            for (SeatResponseDto seatDto : areaDto.getSeats()) {
                Seats seat = Seats.create(
                        area,
                        seatDto.getSeatName(),
                        seatDto.getX(),
                        seatDto.getY(),
                        seatDto.getUiMetadata()
                );
                seatsRepository.save(seat);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ConcertHallResponseDto> getAllConcertHalls() {
        List<ConcertHall> halls = concertHallRepository.findAll();

        return halls.stream().map(hall -> ConcertHallResponseDto.of(
                hall.getId(),
                hall.getConcertHallName(),
                hall.getCreatedAt(),  // createdAt 추가
                hall.getConcertHallAreas().stream().map(area -> ConcertHallAreaRequestDto.of(
                        area.getId(),
                        area.getAreaName(),
                        area.getX(),
                        area.getY(),
                        area.getUiMetadata(),
                        area.getSeats().stream().map(seat -> SeatResponseDto.of(
                                seat.getId(),
                                seat.getSeatName(),
                                seat.getX(),
                                seat.getY(),
                                seat.getUiMetadata()
                        )).collect(Collectors.toList())
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }
}
