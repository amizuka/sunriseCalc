package calc;

public class SunriseCalc {
	
	// 時分秒の緯度経度を度とその小数に変換
	public double conversionDegreesMinitesSecondsToDegreesAndDecimil(int hours, int minute, double second) {
		return hours + (minute * 60 + second) / 3600;
	}
	
	// 赤経を度とその少数に変換するメソッド
	public double conversionRightAscension(int hours, int minutes, double seconds) {
		return hours * 15 + minutes * 0.25 + seconds * 0.0125 / 3;
	}
	
	// 赤緯を変換するメソッド
	public double conversionDeclination(int hours, int minutes, int second) {
		double returnConversionDeclination;
		
		if (hours < 0) {
			returnConversionDeclination = (double) (hours - minutes / 60 - second / 3600);
		} else {
			returnConversionDeclination = (double) -(hours + minutes / 60 + second / 3600);
		}
		
		return returnConversionDeclination;
	}
	
	// 経過時間を一日を 1 とした小数に変換
	public double calcElapsedTime(int hour) {
		return hour / 24.0;
	}
	
	//太陽赤経、赤緯の一次補間 
	public double interpolateOfRightAscensionAndDeclination(double previousRightAcension, double middleRightAcension, 
												double followingRightAcension, double timesOfDay) {
		
		double formulaA = 0.0;
		double formulaB = 0.0;
		if (-0.625 <= timesOfDay && timesOfDay < 0.375) {
			formulaA = middleRightAcension - previousRightAcension;
			formulaB = ((3 * previousRightAcension) + (5 * middleRightAcension)) / 8;
		} else if (0.375 <= timesOfDay && timesOfDay <= 1.375) {
			formulaA = followingRightAcension - middleRightAcension;
			formulaB = ((11 * middleRightAcension) - (3 * followingRightAcension));
		}
		return (formulaA * timesOfDay) + formulaB;
	}
	
	//太陽距離 r の補間
	// r = r1 + (r2 - r1) * ((b - a - 0.375) / 10) + (r2 - r1) * d / 10;
	public double interpolateOfAU(double previousAU, double followingAu, double timesOfDay, int date, 
									int calcDate) {
		double difference = followingAu - previousAU;
		
		return previousAU + difference * (calcDate - date - 0.375) / 10 
				+ difference * (timesOfDay / 10);
	}
	
	// 恒星時を決める
	public double calcSideralTime(double latitude, double grennwichSiderealTime, double timesOfDay) {
		// Φ = ΦG + λ - japanMeridian + increaseSiderealTime * timesOfDay
		final double JAPAN_MERIDIAN = 135.369618;
		final double INCREASE_SIDEREAL_TIME = 360.9856474;
		
		return grennwichSiderealTime + latitude - JAPAN_MERIDIAN + INCREASE_SIDEREAL_TIME * timesOfDay;
	}
	
	// 視半径を求める
	public double calcSunSemidiameter(double sunDistance) {
		final double SEMIDIAMETER = 0.266994;
		return SEMIDIAMETER / sunDistance;
	}
	
	// 視差を求める
	public double calcParallax(double SunDistance) {
		final double PARALLAX = 0.0024428;
		return PARALLAX / SunDistance;
	}
	
	// 出没高度を求める
	public double calcAltitudeOfSun(double sunSemidiameter, double atomosphericRefraction, double parallax) {
		return -sunSemidiameter - 0 - atomosphericRefraction + parallax;
	}
	
	// 出没高度を時角に換算する
	public double calcProvisionalHourAngle(double sunAltitude, double declination, double longitude, double timesOfDay) {
		double sinSunAltitude = Math.sin(Math.toRadians(sunAltitude)); // k
		double sinDeclination = Math.sin(Math.toRadians(declination)); // delta
		double sinLongitude = Math.sin(Math.toRadians(longitude)); // phi
		
		double cosDeclination = Math.cos(Math.toRadians(declination)); // delta
		double cosLongitude = Math.cos(Math.toRadians(longitude)); // phi
		
		double cosSunAltitudeHourAngle = (sinSunAltitude - (sinDeclination * sinLongitude)
																			/ (cosDeclination * cosLongitude));
		if (timesOfDay < 0.5) {
			return -(Math.toDegrees(Math.acos(cosSunAltitudeHourAngle)));
		} else {
			return Math.toDegrees(Math.acos(cosSunAltitudeHourAngle));
		}
	}
	
	public double calcSunHourAngle(double interpolationRightAcention, double siderealTime) {
		return siderealTime - interpolationRightAcention;
	}
	
	public double calcDeltaTimesOfDayValue(double hourAngle, double sunHourAngle) {
		return (hourAngle - sunHourAngle) / 360;
	}
	
	public int[] conversionTimesOfDayValueToTimesOfDay(double timesOfDayValue) {
		timesOfDayValue *= 24;
		int hour = (int) Math.floor(timesOfDayValue);
		int minitesAndSeconds = (int) (Math.floor((timesOfDayValue % 1.0) * 60));
		int[] timesOfDayArray = {hour, minitesAndSeconds};
		
		return timesOfDayArray;
	}
}
