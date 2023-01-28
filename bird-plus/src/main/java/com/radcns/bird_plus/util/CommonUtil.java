package com.radcns.bird_plus.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import com.radcns.bird_plus.util.Converter;
@Component
public class CommonUtil {//extends AwsCliCommand{
	
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	public SimpleDateFormat dateFormat;
	
	public SimpleDateFormat dateTimeFormat;
	
	public Date nowDate;
	
	public String nowDateStr;
	
	public String filePath;
	
	public CommonUtil() {
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.nowDate = new Date(System.currentTimeMillis());
		this.nowDateStr = this.dateFormat.format(this.nowDate);
		this.filePath = System.getProperty("user.home") + "/stockApp/";
	}
	
	/*
	public CommonUtil(String targetDate) throws ParseException {
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.nowDateStr = targetDate;
		this.nowDate = this.dateFormat.parse(this.nowDateStr);
		
	}
	public CommonUtil(boolean bol) throws IOException {
		this(0);
	}
	
	public CommonUtil(int commandNumber) throws IOException {
		changeCliOutput("json");
		this.dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.nowDate = new Date(System.currentTimeMillis());
		this.nowDateStr = this.dateFormat.format(this.nowDate);
		//로그 저장 경로
		this.filePath = System.getProperty("user.home") + "/braze_monitoring_logs/assistant/braze_log/" + "어시스턴트_실행날짜_" + this.nowDateStr+ "_" + "command_" + commandNumber;
		
		Path defultPath = Paths.get(this.filePath);

		//해당 파일의 이름이 이미 있을 경우 이름을 새롭게 지정한다.
		for(int i = 1 ; Files.isDirectory(defultPath, LinkOption.NOFOLLOW_LINKS) ; i++) {
			defultPath = Paths.get("%s_%d/".formatted(this.filePath, i));
		}
		
		this.filePath = defultPath.toString() + "\\";
		
		logger.debug("설정된 풀더 경로 >>>" + this.filePath);
		//this.nowDate = 
	}
	*/

	/**
	 * cli 출력 포맷 변경 함수
	 * @param wantOutput cli 커맨드 어떤 출력 포맷을 원하는지? (json, text 등 (cli config doc 참조))
	 * @throws IOException
	 */
	/*
	protected void changeCliOutput(String wantOutput) throws IOException {
		//aws cli 인증정보 및 설정정보 파일 불러오기
		File credentials_path = new File(System.getProperty("user.home") + USER_PORFILE_CREDENTIALS);
		File config_path = new File(System.getProperty("user.home") + USER_PORFILE_CONFIG);

		try(FileReader credentials = new FileReader(credentials_path);
			FileReader config = new FileReader(config_path);)
		{
			Properties prop_credentials = new Properties();
	        prop_credentials.load(credentials);
	        
	        Properties prop_config = new Properties();
	        prop_config.load(config);
	        
        	if ( Arrays.asList(CLI_OUTPUT_FORMAT).contains(wantOutput) 
					&& wantOutput.equals( prop_config.getProperty("output") ) == false) {
        		
        		logger.warn("cli config changed format >>> : " + wantOutput);
				Process process = Runtime.getRuntime().exec("cmd /c " + CLI_OUTPUT_SET_COMMAND.formatted(wantOutput));
				process.waitFor();
				
			}
        	
		}catch(FileNotFoundException e) {
			
			logger.error("cli 인증정보 파일이 존재하지 않습니다. cli 인증을 먼저 해주시길 바랍니다.", e);
			
		}catch(Exception e) {
			
			logger.error(e.getMessage(), e);
			
		}
	}
	*/

	/**
	 * node들의 순서가 보장되는 json prettyPrinting ( value에 "aa,bb"와 같이 ,가 들어갈 때 한 탭씩 밀리는 현상 있음 추후 수정 필요 )
	 * @param obj json 포맷인 객체
	 * @return
	 */
	public String prettyPrintingJson(Object obj) {
		
		StringBuilder prettyJSONBuilder = new StringBuilder();
		//JSONParser jsonParser = new JSONParser();
		//obj = jsonParser.parse(obj).toString();
		//System.out.println(obj.toString());
		
		String str = obj.toString().trim()
				.replaceAll("\\[", "[\n")
				.replaceAll("\\{", "{\n")
				.replaceAll("\\]", "\n]")
				.replaceAll("\\}", "\n}")
				.replaceAll("\\,", ",\n");
		
		//String strArr[] = obj.toString().trim().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		//for(String item : strArr)
		//System.out.println(str);
		String line = "";
		long tab = 1;
		long prevTab = 0;
		
		try(BufferedReader br = new BufferedReader( new StringReader(str) );){
			while ((line = br.readLine()) != null) {
				line = line.trim();
				Character c = ' ';
				boolean lastTextComma = false;
				//콤마 여부에 따라 마지막 글자 자르는 부분을 분기처리
				if(line.isBlank() == false) {
					lastTextComma = line.charAt(line.length()-1) == ',';
					if(lastTextComma) {
						c = line.charAt(line.length()-2);
					}else {
						c = line.charAt(line.length()-1);
					}
				}
				
				//제이슨 시작문자열일 경우 탭 추가 후 tab,prevTab ++
				if(c.equals('{') || c.equals('[')) {
					//tab변수와 prevTab 변수의 차이를 tab-prevTab = 1 이 나오게 만든다.
					for(;tab - prevTab > 1;) {
						if(tab>prevTab) {
							tab--;
						}else {
							//로직이 오류일 경우 무한루프 방지
							break;
						}
					}
					
					line = String.format("%"+tab*4+"s", "") + line;
					tab++;
					prevTab++;
					
				//제이슨 종료 문자열일 경우 탭 추가후 tab변수와 prevTab변수를 일치시킨 후 prevTab-- 시킨다.
				}else if(c.equals('}') || c.equals(']')) {
					
					//제이슨 종료 문자열일 경우 탭을 prevTab으로 준다.
					line = String.format("%"+prevTab*4+"s", "") + line;
					
					//해당 부분으로 마지막 문자에 Comma가 있는 경우 tab - prevTab = 2가 된다. 
					//2를 이용하여 콤마여부에 따라 탭이 다르게 추가되도록 만든다.
					if( lastTextComma == false ) {
						tab = prevTab;
					}
					prevTab --;

				//제이슨 종료,시작 문자열이 아닐 경우 탭만 추가해준다.
				}else{
					//tab변수와 prevTab 변수의 차이를 tab-prevTab = 1 이 나오게 만든다.
					for(;tab - prevTab > 1;) {
						if(tab>prevTab) {
							tab--;
						}else {
							//로직이 오류일 경우 무한루프 방지
							break;
						}
					}
					
					line = String.format("%"+tab*4+"s", "") + line;
				}

				prettyJSONBuilder.append(line+"\n");
			}
		}catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return prettyJSONBuilder.toString();
	}
	
	/**
	 * 내용을 json 파일로 저장시키는 함수
	 * @param str 저장 시킬 내용
	 * @param fileName 저장 할 파일의 이름
	 * @return 
	 */
	public Path fileSave(Object str, String fileName, String format) {
		String dir = this.filePath + fileName;
		dir = dir.replaceAll("/", "\\\\");
		Path directoryPath = Paths.get(dir.substring(0, dir.lastIndexOf("\\")));
		Path filePath = Paths.get(dir + "_0." + format);
		
		//해당 파일의 이름이 이미 있을 경우 이름을 새롭게 지정한다.
		for(int i = 1 ;Files.exists(filePath, LinkOption.NOFOLLOW_LINKS); i++) {
			filePath = Paths.get(dir + "_%d.%s".formatted(i, format));
		}
		
		try {
			//해당 디렉터리에 경로 중 존재하지 않는 풀더가 있을 시 풀더 생성
			Files.createDirectories(directoryPath);
			
			try(BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE);){
				writer.write(str.toString());
				writer.flush();
			} catch(IOException e) {
				logger.error(e.getMessage(), e);
			} catch(NullPointerException e){
				//logger.error("저장할 내용이 비어있습니다. : " + e.getMessage() ,e);
			}finally {
				if(Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
					logger.debug( "%s에 저장 성공하였습니다.".formatted(filePath.toString()) );
					return filePath;
				}else {
					logger.error( "======%s에 저장 실패========".formatted(filePath.toString()) );
				}
			}
		}catch(AccessDeniedException e) {
			logger.error("해당하는 경로에 접근 권한이 없어 로그 저장에 실패하였습니다. : " + e.getMessage(),e);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
		
	} 
	/**
	 * zip 파일을 읽어서 전역 경로에 저장하는 함수
	 * @param zipPath zip 파일이 위치한 경로 Path 객체
	 * @param nameAndExtension 저장 할 파일 이름과 확장자
	 * @Param isComplteAfterZipDelete zip을 풀고 난 후 해당 zip을 제거할지 여부로 true이면 해당 집파일 제거
	 * @return 
	 */
	public Path zipDecompress(Path zipPath, String nameAndExtension, boolean isComplteAfterZipDelete) {
		if(zipPath == null) {
			logger.error("=======================================");
			logger.error("ZIP Is Null Maybe ZIP File Url Is Empty");
			logger.error("=======================================");
		}else {
			Path savePath = Paths.get( this.filePath + nameAndExtension );
			long size = 0;
			try( ZipFile zipFile = new ZipFile(zipPath.toFile()); 
					BufferedWriter writer = Files.newBufferedWriter(savePath, StandardCharsets.UTF_8
							, StandardOpenOption.CREATE
							, StandardOpenOption.SYNC);	
				){
				// zip 내에 위치한 파일 객체
				Enumeration<? extends ZipEntry> entry = zipFile.entries();
				// 하나씩 순회한다.
				while(entry.hasMoreElements()) {
					ZipEntry zipEntry = entry.nextElement();
					try( BufferedReader reader = new BufferedReader( new InputStreamReader( zipFile.getInputStream(zipEntry), StandardCharsets.UTF_8) ) ){
						String line;
						while( (line = reader.readLine() ) != null ) {
							writer.write(line);
						}
						writer.flush();
					}
				}
				size = Files.size(savePath);
				if(Files.exists(savePath, LinkOption.NOFOLLOW_LINKS) && size > 0) {
					logger.debug("Save Complate Path ::: " + savePath.toString());
					return savePath;
				}
			} catch (ZipException e) {
				logger.error("Zip Error >>> " + e.getMessage(), e);
			} catch (IOException e) {
				logger.error("IO Error >>> " + e.getMessage(), e);
			} finally {
				if(isComplteAfterZipDelete) {
					try {
						Files.deleteIfExists(zipPath);
					} catch (IOException e) {
						logger.error("Zip File Delete Failed >>> " + e.getMessage(), e);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Map 객체를 Record 객체로 변환하는 함수
	 * @param <T>
	 * @param targetClass 변환하고자 하는 record의 class
	 * @param data record로 변환할 map data
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Record> T transformMapToRecord(Class<T> targetClass, Map data) {
		try {
			// 타겟이 되는 record calss의 구성요소를 순서대로 반환
			RecordComponent[] component = targetClass.getRecordComponents();
			// record 컴포넌트의 길이
			int len = component.length;
			// record 클래스를 선언시 들어갈 parameter
			Object[] params = new Object[len];
			// 각 컴포넌트들의 클래스 타입을 지정할 배열
			Class<?>[] componentsType = new Class<?>[len];

			// 생성자에 들어갈 parameter, class type을 넣어준다.
			for(len-- ; len >= 0 ; len--) {
				// 컴포넌트의 타입을 넣어준다.
				componentsType[len] = component[len].getType();
				// 데이터를 컴포넌트의 타입으로 캐스팅하여 넣어준다.
				params[len] = Converter.CALSS_CONVERT_VALUE.get(component[len].getType())
						.apply(data.get( data.get(component[len].getName()) ));
						//component[len].getType().cast( data.get(component[len].getName()) );
			}
			
			//생성자에 컴포넌트들의 타입을 넣어준다.
			Constructor<T> target = targetClass.getDeclaredConstructor(componentsType);
			// 타겟이 되는 record를 new 한다.
			return target.newInstance(params);
		} catch (NoSuchMethodException | SecurityException | InstantiationException 
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NullPointerException | ClassCastException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T transformMapToEntity(Class<T> targetClass, Map data) {
		try {
			// 엔티티의 필드 가져오기
			Field[] fields = targetClass.getDeclaredFields();
			// 엔티티 필드의 길이
			// 생성자에 들어갈 parameter
			List<Object> params = new ArrayList<>();
			// 각 컴포넌트들의 클래스 타입을 지정할 배열
			List<Class<?>> componentsType = new ArrayList<>();

			// 생성자에 들어갈 parameter, class type을 넣어준다.
			for(int i = 0 , len = fields.length ; i < len ; i += 1) {

				//Transient 어노테이션이 존재하는 필드는 생성자에서 제외시킨다.
				//if(fields[i].getAnnotationsByType(org.springframework.data.annotation.Transient.class).length > 0) {
				//	continue;
				//}
				
				// 컴포넌트의 타입을 넣어준다.
				componentsType.add( fields[i].getType() );
				
				// 데이터를 컴포넌트의 타입으로 캐스팅하여 넣어준다.
				Object targetItem = data.get(fields[i].getName());
				if( targetItem != null &&
					fields[i].getType().equals( targetItem.getClass() ) == false 
				){
					
					params.add(
						Converter.CALSS_CONVERT_VALUE.get(fields[i].getType())
						.apply( data.get(fields[i].getName()) )
					);
					
				}else {
					params.add( data.get(fields[i].getName()) );
				}

				
			}
			
			//생성자에 컴포넌트들의 타입을 넣어준다.
			Constructor target = targetClass.getDeclaredConstructor(componentsType.toArray(Class[]::new));
			// 타겟이 되는 엔티티를 생성 한다.
			return targetClass.cast(target.newInstance(params.toArray()));
		} catch (NoSuchMethodException | SecurityException | InstantiationException 
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NullPointerException | ClassCastException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 날짜기준으로만 계산 (날짜만 필요한 경우) 
	 * @param day 빼고자 할 일수
	 * @return
	 */
	public Calendar mathSubDayOnlyDate(int day) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(this.dateFormat.parse(this.nowDateStr));
		} catch (ParseException e) {
			cal.setTime(this.nowDate);
		}
		cal.add(Calendar.DATE, -1*day);
		
		return cal;
	}
	
	/**
	 * 시간까지 포함해서 계산 (시간도 같이 필요한 경우)
	 * @param day 빼고자 할 일수
	 * @return
	 */
	public Calendar mathSubDay(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.nowDate);
		cal.add(Calendar.DATE, -1*day);
		
		return cal;
	}
	
	public Path IfNoSuchFileToCreate(Path path) {
		String pathStr = path.toString();
		Path createPath = Paths.get(pathStr.substring(0, pathStr.lastIndexOf("\\")));
		try {
			Files.createDirectories(createPath);
		} catch (IOException e) {
			return null;
		}
		return path;
	}
	
}
