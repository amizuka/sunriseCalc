package calc;

public class CalcJan02Sunrise {
	SunriseCalc sc = new SunriseCalc();
	// 2019年1月2日の日の出時刻を求めるための情報
	// 3日分の変換した世界時0時における赤経
	double rightAcention1 = sc.conversionRightAscension(18, 44, 37.6);
	double rightAcention2 = sc.conversionRightAscension(18, 49, 2.5);
	double rightAcention3 = sc.conversionRightAscension(18, 53, 27.1);

	// 3日分の変換した世界時0時における赤緯
	double declination1 = sc.conversionDeclination(-23, 2, 20);
	double declination2 = sc.conversionDeclination(-22, 57, 24);
	double declination3 = sc.conversionDeclination(-22, 52, 0);

	// 太陽距離
	final double AU_1 = 0.98331;
	final double AU_2 = 0.98331;

	// 恒星時
	final double GREENWICH_SIDEREAL_TIME = sc.conversionRightAscension(6, 45, 22.2);

	// 大気差 R
	final double ATOMOSPHERIC_REFRACTION = sc.conversionDegreesMinitesSecondsToDegreesAndDecimil(0, 35, 8.0);

	// 東京の緯度経度
	final double LATITUDE_OF_TOKYO = sc.conversionDegreesMinitesSecondsToDegreesAndDecimil(139, 44, 40.9);
	final double LONGITUDE_OF_TOKYO = sc.conversionDegreesMinitesSecondsToDegreesAndDecimil(35, 39, 16.0);

	// 仮の時刻変数 d
	double timesOfDayValue = sc.calcElapsedTime(6);
	double deltatimesOfDayValue = 0.1;

	public String calcSunrise() {

		// 太陽赤経を求める
		double interpolationRightAcention = sc.interpolateOfRightAscensionAndDeclination(rightAcention1,
				rightAcention2, rightAcention3, timesOfDayValue);

		// 太陽赤緯を求める
		double interpolationDeclination = sc.interpolateOfRightAscensionAndDeclination(declination1,
				declination2, declination3, timesOfDayValue);

		// 太陽距離を求める
		double interpolationSunDistance = sc.interpolateOfAU(AU_1, AU_2, timesOfDayValue, 1, 2);

		double tokyoSiderealTime = sc.calcSideralTime(LATITUDE_OF_TOKYO, GREENWICH_SIDEREAL_TIME, timesOfDayValue);

		// 視半径 S
		double sunSemidiameter = sc.calcSunSemidiameter(interpolationSunDistance);

		// 視差
		double parallax = sc.calcParallax(interpolationSunDistance);

		// 出没高度
		double sunAltitude = sc.calcAltitudeOfSun(sunSemidiameter, ATOMOSPHERIC_REFRACTION, parallax);
		double hourAngle = sc.calcProvisionalHourAngle(sunAltitude, interpolationDeclination, LONGITUDE_OF_TOKYO,
				timesOfDayValue);
		double sunHourAngle = sc.calcSunHourAngle(interpolationRightAcention, tokyoSiderealTime);
		deltatimesOfDayValue = sc.calcDeltaTimesOfDayValue(hourAngle, sunHourAngle);

		timesOfDayValue += deltatimesOfDayValue;
		
		if (0.00005 < deltatimesOfDayValue) {
			calcSunrise();
		}
		
		String ans = "2019年1月2日の日の出時刻はおよそ" + sc.conversionTimesOfDayValueToTimesOfDay(timesOfDayValue)[0]
				+ "時" + sc.conversionTimesOfDayValueToTimesOfDay(timesOfDayValue)[1] + "分";
		return ans;

	}

}
