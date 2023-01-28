package com.radcns.bird_plus.util;


import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.util.Map.ofEntries;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.util.Map.entry;

public class Converter {

	private static LocalDate parseDate(Object data, String format) {
		if(data.getClass().equals(String.class)) {
			return LocalDate.parse((String)data, DateTimeFormatter.ofPattern(format));
		}else if(data.getClass().equals(Long.class)) {
			return Instant.ofEpochMilli((Long)data)
			        .atZone(ZoneId.systemDefault()) // default zone
			        .toLocalDate();
		}else {
			return null;
		}
	}
	private static LocalDateTime parseDateTime(Object data, String format) {
		if(data.getClass().equals(String.class)) {
			return LocalDateTime.parse((String)data, DateTimeFormatter.ofPattern(format));
		}else if(data.getClass().equals(Long.class)) {
			return Instant.ofEpochMilli((Long)data)
			        .atZone(ZoneId.systemDefault()) // default zone
			        .toLocalDateTime();
		}else {
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static final CopyOnWriteArrayList<Map<String, Object>> DATE_FORMAT_CONVERT = new CopyOnWriteArrayList<>(List.of(
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{8}$")), 
			entry("convert", (Function<Object, LocalDate>)(date) -> parseDate(date, "yyyyMMdd")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$")), 
			entry("convert", (Function<Object, LocalDate>)(date) -> parseDate(date, "yyyy-MM-dd")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}-\\d{1,2}-\\d{4}$")), 
			entry("convert", (Function<Object, LocalDate>)(date) -> parseDate(date, "dd-MM-yyyy")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{4}$")), 
			entry("convert", (Function<Object, LocalDate>)(date) -> parseDate(date, "MM/dd/yyyy")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2}$")), 
			entry("convert", (Function<Object, LocalDate>)(date) -> parseDate(date, "yyyy/MM/dd")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$")), 
			entry("convert", (Function<Object, LocalDate>)(date) -> parseDate(date, "dd MMM yyyy")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$")), 
			entry("convert", (Function<Object, LocalDate>)(date) -> parseDate(date, "dd MMMM yyyy")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$")), 
			entry("convert", (Function)(date) -> parseDateTime(date, "yyyy-MM-dd HH:mm:ss")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{12}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "yyyyMMddHHmm")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{8}\\s\\d{4}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "yyyyMMdd HHmm")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "dd-MM-yyyy HH:mm")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "yyyy-MM-dd HH:mm")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "MM/dd/yyyy HH:mm")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "yyyy/MM/dd HH:mm"))
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "dd MMM yyyy HH:mm")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "dd MMMM yyyy HH:mm")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{14}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "yyyyMMddHHmmss")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{8}\\s\\d{6}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "yyyyMMdd HHmmss")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "dd-MM-yyyy HH:mm:ss")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "MM/dd/yyyy HH:mm:ss")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "yyyy/MM/dd HH:mm:ss")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "dd MMM yyyy HH:mm:ss")) 
		),
		ofEntries( 
			entry("pattern", Pattern.compile("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$")), 
			entry("convert", (Function<Object, LocalDateTime>)(date) -> parseDateTime(date, "dd MMMM yyyy HH:mm:ss")) 
		)
	));
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final Map<Class, Function> CALSS_CONVERT_VALUE = ofEntries(
		entry(Integer.class, (Function<Object, Integer>) (data) -> {
			if(data.getClass().equals(String.class)){
				String str = (String)data;
				if(str.isBlank() || str == null) {
					str = "0";
				}
				return Integer.valueOf(str);
			}
			return null;
		}),
		
		entry(Long.class, (Function<Object, Long>) (data) -> {
			if(data.getClass().equals(String.class)){
				String str = (String)data;
				if(str.isBlank() || str == null) {
					str = "0";
				}
				return Long.valueOf(str);
			}
			return null;
		}),
		
		entry(String.class, (Function<Object, String>) (data) -> String.valueOf(data) ),
		
		entry(LocalDate.class, (Function<Object, LocalDate>) (data) ->  {
			return (LocalDate) ((Function)DATE_FORMAT_CONVERT.stream()
				.filter(e->((Pattern)e.get("pattern")).matcher((String)data).matches())
				.findFirst()
				.orElse( Map.of("convert", (Function) (empty) -> parseDate("19700101", "yyyyMMdd")) )
				.get("convert")).apply(data);
		}),
		
		entry(LocalDateTime.class, (Function<Object,LocalDateTime>) (data) ->  {
			return (LocalDateTime) ((Function)DATE_FORMAT_CONVERT.stream()
				.filter(e->((Pattern)e.get("pattern")).matcher((String)data).matches())
				.findFirst()
				.orElse( Map.of("convert", (Function) (empty) -> parseDateTime("19700101 00:00:00", "yyyyMMdd HH:mm:ss")) )
				.get("convert")).apply(data);
		})
	);
}
