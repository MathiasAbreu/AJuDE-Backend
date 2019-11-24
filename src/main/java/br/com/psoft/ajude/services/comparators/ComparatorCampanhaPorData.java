package br.com.psoft.ajude.services.comparators;

import br.com.psoft.ajude.entities.Campanha;

import java.util.Comparator;

public class ComparatorCampanhaPorData implements Comparator<Campanha> {

    @Override
    public int compare(Campanha campanha01, Campanha campanha02) {

        return campanha01.getDataDeadline().compareTo(campanha02.getDataDeadline());
    }
}
