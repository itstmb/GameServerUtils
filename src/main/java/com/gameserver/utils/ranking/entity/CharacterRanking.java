package com.gameserver.utils.ranking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CharacterRanking {

    @JsonIgnore
    private int id;

    private String name;
    private String guild;
    private int job;
    private int level;
    private int experience;
    private int fame;

    public CharacterRanking() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGuild() {
        return guild;
    }

    public int getJob() {
        return job;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getFame() {
        return fame;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGuild(String guild) {
        this.guild = guild;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }
}
