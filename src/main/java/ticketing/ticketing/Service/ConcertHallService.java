package ticketing.ticketing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketing.ticketing.DTO.*;
import ticketing.ticketing.Repository.AdminRepository;
import ticketing.ticketing.Repository.ConcertHallAreaRepository;
import ticketing.ticketing.Repository.ConcertHallRepository;
import ticketing.ticketing.Repository.SeatsRepository;
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

        for (ConcertHallAreaRequestDto areaDto : dto.getAreas()) {
            ConcertHallArea area = ConcertHallArea.create(
                    areaDto.getAreaName(),
                    concertHall,
                    areaDto.getX(),
                    areaDto.getY(),
                    areaDto.getUiMetadata()
            );
            concertHallAreaRepository.save(area);

            for (SeatRequestDto seatDto : areaDto.getSeats()) {
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
                hall.getConcertHallAreas().stream().map(area -> ConcertHallAreaResponseDto.of(
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
