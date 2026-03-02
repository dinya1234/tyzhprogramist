package ru.shop.tyzhprogramist.tyzhprogramist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="component_types")
public class ComponentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "order_ step", nullable = false) //Порядок шагов в конфигураторе
    private Integer order_step;

    public ComponentType(){}

    public ComponentType(Integer order_step, String name){
        this.name= name;
        this.order_step= order_step;
    }

}
