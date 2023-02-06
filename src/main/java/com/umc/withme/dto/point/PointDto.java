package com.umc.withme.dto.point;

import com.umc.withme.domain.Point;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PointDto {

    private Long id;
    private Integer attendance;
    private Integer passion;
    private Integer contactSpeed;

    public static PointDto of(Long id, Integer attendance, Integer passion, Integer contactSpeed) {
        return new PointDto(id, attendance, passion, contactSpeed);
    }

    public static PointDto from(Point point) {
        return PointDto.of(point.getId(), point.getAttendance(), point.getPassion(), point.getContactSpeed());
    }

    public Point toEntity() {
        return Point.builder()
                .attendance(this.getAttendance())
                .passion((this.getPassion()))
                .contactSpeed(this.getContactSpeed())
                .build();
    }
}
