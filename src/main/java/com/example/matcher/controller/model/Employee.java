package com.example.matcher.controller.model;

import com.opencsv.bean.CsvBindByName;

public class Employee {

    //region Properties
    @CsvBindByName
    private String name;

    @CsvBindByName
    private String email;

    @CsvBindByName
    private String division;

    @CsvBindByName
    private short age;

    @CsvBindByName
    private short timezone;

    @CsvBindByName
    private String location;

    @CsvBindByName(column = "sameLocationPreference" )
    private String sameLocationPreference;
    //endregion

    //region Constructors
    public Employee() {
    }

    public Employee(final String name, final String email,
                    final String division,
                    final short age,
                    final short timezone,
                    final String location) {
        this.name = name;
        this.email = email;
        this.division = division;
        this.age = age;
        this.timezone = timezone;
        this.location = location;

    }
    //endregion

    //region Getters, Setters
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(final String division) {
        this.division = division;
    }

    public short getAge() {
        return age;
    }

    public void setAge(final short age) {
        this.age = age;
    }

    public short getTimezone() {
        return timezone;
    }

    public void setTimezone(final short timezone) {
        this.timezone = timezone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSameLocationPreference() {
        return sameLocationPreference;
    }

    public void setSameLocationPreference(String sameLocationPreference) {
        this.sameLocationPreference = sameLocationPreference;
    }

    //endregion

}
