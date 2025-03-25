package b1nd.dodam.domain.rds.bus.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity(name = "bus_time_to_bus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusTimeToBus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_busTime_id")
    private BusTime busTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_bus_id")
    private Bus bus;

    @Builder
    public BusTimeToBus(BusTime busTime, Bus bus) {
        this.busTime = busTime;
        this.bus = bus;
    }
}
