package com.newfeds.weatherguru.data;

/**
 * Created by GT on 1/3/2016.
 */
public class WeatherData {
    private int ivWeatherIcon;
    private String day;
    private String description;
    private double tempMin;
    private double tempMax;
    private double humidity;
    private double pressure;

    public WeatherData() {
    }

    public WeatherData(int icon, String day, String description, double tempMin, double tempMax, double humidity, double pressure) {
        this();
        this.setIcon(icon);
        this.setDay(day);
        this.setDescription(description);
        this.setTempMin(tempMin);
        this.setTempMax(tempMax);
        this.setHumidity(humidity);
        this.setPressure(pressure);

    }


    public int getIcon() {
        return ivWeatherIcon;
    }

    public void setIcon(int icon) {
        this.ivWeatherIcon = icon;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
