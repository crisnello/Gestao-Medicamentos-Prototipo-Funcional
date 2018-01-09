package br.com.bradesco.web.entitie.geo;


public class GeoUtils {

	public static int EARTH_RADIUS_KM = 6371;

	/**
	 * Distância entre dois pontos geográficos. Os valores devem ser informados
	 * em graus.
	 * 
	 * @param firstLatitude
	 *            Latitude do primeiro ponto
	 * @param firstLongitude
	 *            Longitude do primeiro ponto
	 * @param secondLatitude
	 *            Latitude do segundo ponto
	 * @param secondLongitude
	 *            Longitude do segundo ponto
	 * 
	 * @return Distância em quilômetros entre os dois pontos
	 */
	public static double geoDistanceInKm(double firstLatitude, double firstLongitude, double secondLatitude, double secondLongitude) {

		// Conversão de graus pra radianos das latitudes
		double firstLatToRad = Math.toRadians(firstLatitude);
		double secondLatToRad = Math.toRadians(secondLatitude);

		// Diferença das longitudes
		double deltaLongitudeInRad = Math.toRadians(secondLongitude
				- firstLongitude);

		// Cálcula da distância entre os pontos
		return Math.acos(Math.cos(firstLatToRad) * Math.cos(secondLatToRad)
				* Math.cos(deltaLongitudeInRad) + Math.sin(firstLatToRad)
				* Math.sin(secondLatToRad))
				* EARTH_RADIUS_KM;
	}

	/**
	 * Distância entre dois pontos geográficos.
	 * 
	 * @param first
	 *            Primeira coordenada geográfica
	 * @param second
	 *            Segunda coordenada geográfica
	 * @return Distância em quilômetros entre os dois pontos
	 */
	public static double geoDistanceInKm(GeoCoordinate first, GeoCoordinate second) {
		return geoDistanceInKm(first.getLatitude(), first.getLongitude(), second.getLatitude(), second.getLongitude());
	}

	
	public static double getAngle(GeoCoordinate first,	GeoCoordinate second){
		double  lat1 = first.getLatitude(), 
				lon1 = first.getLongitude(), 
				lat2 = second.getLatitude(), 
				lon2 = second.getLongitude();
		
		
			double longDiff= lon2-lon1;
		   double y= Math.sin(longDiff)*Math.cos(lat2);
		   double x=Math.cos(lat1)*Math.sin(lat2)-   Math.sin(lat1)*Math.cos(lat2)*Math.cos(longDiff);

	    
	    return (Math.toDegrees(Math.atan2(y, x))+360)%360;
	}
}