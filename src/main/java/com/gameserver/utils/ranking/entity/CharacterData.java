package com.gameserver.utils.ranking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"character", "discord"})
public class CharacterData {

    @JsonProperty("character")
    private CharacterRanking characterRanking;
    private String discord;

    public CharacterData(CharacterRanking characterRanking, String discord) {
        this.characterRanking = characterRanking;
        this.discord = discord;
    }

    public String getDiscord() {
        return discord;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }
}
