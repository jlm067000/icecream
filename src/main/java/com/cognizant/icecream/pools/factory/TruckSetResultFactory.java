package com.cognizant.icecream.pools.factory;

import com.cognizant.icecream.models.Truck;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TruckSetResultFactory extends ServiceResultFactory<Set<Truck>> {}
