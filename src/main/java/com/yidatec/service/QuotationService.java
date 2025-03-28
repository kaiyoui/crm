package com.yidatec.service;

import com.yidatec.mapper.ProductMapper;
import com.yidatec.mapper.ProjectMapper;
import com.yidatec.mapper.QuotationMapper;
import com.yidatec.model.Product;
import com.yidatec.util.CNNumberFormat;
import com.yidatec.vo.QuotationVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报价单
 *
 * @author jrw
 *
 */
@Service("quotationService")
public class QuotationService {

	@Autowired
	QuotationMapper quotationMapper;

	@Autowired
	ProjectMapper projectMapper;

	@Autowired
	ProductMapper productMapper;

	/**
	 * 消息资源定义文件
	 */
	private String LOGO_IMG = "logo.png";

	public void quotationDownLoad(XSSFWorkbook wb,
								  String id) throws Exception {

		HashMap<String,List<QuotationVO>> map = new HashMap<String,List<QuotationVO>>();

		List<QuotationVO> quotationVOList =  quotationMapper.quotationDownLoad(id);

		List<String> dicIdList =  new ArrayList<String>();

		for (QuotationVO quotationVO : quotationVOList) {

			if(!dicIdList.contains(quotationVO.getDicId())){
				dicIdList.add(quotationVO.getDicId());
			}

			List<QuotationVO> quotationVOList1 = map.get(quotationVO.getDicId());

			if (quotationVOList1 == null){
				quotationVOList1 = new ArrayList<>();
				map.put(quotationVO.getDicId(),quotationVOList1);
			}

			quotationVOList1.add(quotationVO);
		}

		XSSFSheet sheet = wb.createSheet("sheet1");
		Map<String, XSSFCellStyle> mapStyle = createStyles(wb);
		sheet.setDisplayGridlines(false);

		// 起始行号
		XSSFRow row = null;
		// 设定标题行号，生成标题
		int rowNum = 0;
//		row = sheet.createRow(rowNum);

//		row.createCell(0).setCellValue("总标题");
//		setColspanTitle(sheet,row , mapStyle, wb,
//				quotationVOList!=null && quotationVOList.size() > 0   ? quotationVOList.get(0).getProjectName()+"报价单":"报价单", "header_6",
//				0, 0, 0, 0, 7);
//		sheet.setColumnWidth(0, 32 * 50);


		for (int r = 0; r < 4; r++) {
			row = sheet.createRow(r);
			for (int i = 0; i < 8; i++) {
				row.createCell(i).setCellValue("");
				row.getCell(i).setCellStyle(mapStyle.get("header_7"));
				// 1行
				if(r == 0 && i == 5) {
					// 参数：起始行号，终止行号， 起始列号，终止列号
					CellRangeAddress region = new CellRangeAddress(0,0,0,5);
					sheet.addMergedRegion(region);
					row.getCell(0).setCellValue("项目报价单");
				}else if(r == 0 && i == 7){
					row.getCell(7).setCellValue("上海鼎世展览展示有限公司");
				}

				// 2行
				if(r== 1 && i == 0){
					row.getCell(i).setCellValue("项目名称：");
				}
				else if(r== 1 && i == 4) {
					// 参数：起始行号，终止行号， 起始列号，终止列号
					CellRangeAddress region = new CellRangeAddress(1,1,1,4);
					sheet.addMergedRegion(region);
					row.getCell(1).setCellValue(quotationVOList!=null && quotationVOList.size() > 0   ? quotationVOList.get(0).getProjectName():"");
					row.getCell(1).setCellStyle(mapStyle.get("header_8"));
				}

				// 3行
				if(r== 2 && i == 0){
					row.getCell(i).setCellValue("项目面积：");
				}else if(r == 2 && i == 7){
					row.getCell(7).setCellValue("Shanghai Deosin Exbition Co.Ltd");
				}
				else if(r== 2 && i == 4) {
					// 参数：起始行号，终止行号， 起始列号，终止列号
					CellRangeAddress region = new CellRangeAddress(2,2,1,4);
					sheet.addMergedRegion(region);
					row.getCell(1).setCellValue(quotationVOList!=null && quotationVOList.size() > 0   ? quotationVOList.get(0).getArea() +"m²":"");
					row.getCell(1).setCellStyle(mapStyle.get("header_8"));
				}
				// 4行
				if(r== 3 && i == 0){
					row.getCell(i).setCellValue("项目时间：");
				}
				else if(r== 3 && i == 4) {

					CellRangeAddress region = new CellRangeAddress(3,3,1,4);
					sheet.addMergedRegion(region);
					row.getCell(1).setCellValue(quotationVOList!=null && quotationVOList.size() > 0   ? quotationVOList.get(0).getStartDate().substring(0,10):"");
					row.getCell(1).setCellStyle(mapStyle.get("header_8"));
				}

//				// 第7列
				if(r == 3 && i == 6){
					CellRangeAddress region = new CellRangeAddress(0,3,6,6);
					sheet.addMergedRegion(region);
//					// logo
//					String path =  "classpath:\\static\\template\\logo.png";
//                    col1：起始单元格列序号，从0开始计算；
//                    row1：起始单元格行序号，从0开始计算，如例子中col1=0,row1=0就表示起始单元格为A1；
//                    col2：终止单元格列序号，从0开始计算；
//                    row2：终止单元格行序号，从0开始计算，如例子中col2=2,row2=2就表示起始单元格为C3；
					drawingImage(sheet,(short)6,0,(short)(6+1),4);
				}

				// 第8列
				if( r == 1 && i == 7){

					CellRangeAddress region = new CellRangeAddress(0,1,7,7);
					sheet.addMergedRegion(region);
				}
				if(r==3 && i == 7){
					CellRangeAddress region = new CellRangeAddress(2,3,7,7);
					sheet.addMergedRegion(region);
				}
			}
			rowNum = r;


		}



		rowNum++;// 从第二行起
		row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue("类别");
		sheet.setColumnWidth(0, 32 * 80);
		row.createCell(1).setCellValue("编号");
		sheet.setColumnWidth(1, 32 * 50);
		row.createCell(2).setCellValue("产品名称");
		sheet.setColumnWidth(2, 32 * 150);
		row.createCell(3).setCellValue("单位");
		sheet.setColumnWidth(3, 32 * 50);
		row.createCell(4).setCellValue("数量");
		sheet.setColumnWidth(4, 32 * 50);
		row.createCell(5).setCellValue("单价(元)");
		sheet.setColumnWidth(5, 32 * 80);
		row.createCell(6).setCellValue("合价(元)");
		sheet.setColumnWidth(6, 32 * 90);
		row.createCell(7).setCellValue("工作内容");
		sheet.setColumnWidth(7, 32 * 500);

		// 第2行以上冻结
//		sheet.createFreezePane(0, 2, 0, 2);

		row.getCell(0).setCellStyle(mapStyle.get("header_7"));
		row.getCell(1).setCellStyle(mapStyle.get("header_7"));
		row.getCell(2).setCellStyle(mapStyle.get("header_7"));
		row.getCell(3).setCellStyle(mapStyle.get("header_7"));
		row.getCell(4).setCellStyle(mapStyle.get("header_7"));
		row.getCell(5).setCellStyle(mapStyle.get("header_7"));
		row.getCell(6).setCellStyle(mapStyle.get("header_7"));
		row.getCell(7).setCellStyle(mapStyle.get("header_7"));
		row.setHeightInPoints(30);

		int num = 1;
		Double countPrict = new Double(0);
        /*设置数据起始行号，填充数据*/
		for (String s : dicIdList) {
			List<QuotationVO> quotationVOList2 = map.get(s);
			for (int i = 0; i< quotationVOList2.size() ; i ++) {
				QuotationVO quotationVO = quotationVOList2.get(i);
				row = sheet.createRow(++rowNum);
				// 类别
				row.createCell(0);

				row.getCell(0).setCellValue(quotationVO.getCategoryName());

				if(i == quotationVOList2.size()-1){
					if ( quotationVOList2.size() >1) {
						// 参数：起始行号，终止行号， 起始列号，终止列号
						CellRangeAddress region = new CellRangeAddress(rowNum - quotationVOList2.size() + 1, rowNum, 0, 0);
						sheet.addMergedRegion(region);
					}
				}

				// 编号
				row.createCell(1).setCellValue(num++);
				// 产品名称
				row.createCell(2).setCellValue(quotationVO.getProductName());
				// 单位
				row.createCell(3).setCellValue(quotationVO.getUnitName());
				// 数量
				row.createCell(4).setCellValue(quotationVO.getCount());
				// 单价
				row.createCell(5).setCellValue(Double.valueOf(quotationVO.getUnitPrice()));
				// 合价
				row.createCell(6).setCellValue(Double.valueOf(quotationVO.getCountPrice()));
				countPrict += Double.valueOf(quotationVO.getCountPrice());
				// 工作内容
				row.createCell(7).setCellValue(quotationVO.getWorkContent());

				row.getCell(0).setCellStyle(mapStyle.get("data_4"));
				row.getCell(1).setCellStyle(mapStyle.get("data_4"));
				row.getCell(2).setCellStyle(mapStyle.get("data_1"));
				row.getCell(3).setCellStyle(mapStyle.get("data_4"));
				row.getCell(4).setCellStyle(mapStyle.get("data_4"));
				row.getCell(5).setCellStyle(mapStyle.get("data_5"));
				row.getCell(6).setCellStyle(mapStyle.get("data_5"));
				row.getCell(7).setCellStyle(mapStyle.get("data_1"));
				row.setHeightInPoints(20);
			}
		}

		if(quotationVOList != null && quotationVOList.size() > 0) {
			int lastNumb = rowNum + 1;

			rowNum++;
			row = sheet.createRow(rowNum);
			// 施工费用合计
			row.createCell(0).setCellValue("施工费用合计");
			setColspanTitle(sheet, row, mapStyle, wb,
					"施工费用合计", "data_6",
					0, rowNum, rowNum, 0, 5);

			// 施工费用合计钱数
			row.createCell(6).setCellFormula("SUM(G3:G" + lastNumb + ")");
			row.getCell(6).setCellStyle(mapStyle.get("data_7"));

			// 施工费用合计钱数转汉子
			CNNumberFormat cnFmt = new CNNumberFormat(true);
			row.createCell(7).setCellValue("RMB" + cnFmt.format(countPrict));
			row.getCell(7).setCellStyle(mapStyle.get("data_8"));

			rowNum++;
			row = sheet.createRow(rowNum);
			// 税费
			row.createCell(0).setCellValue("税费(6%,增票专票)");
			setColspanTitle(sheet, row, mapStyle, wb,
					"税费(6%,增票专票)", "data_9",
					0, rowNum, rowNum, 0, 5);

			// 税费钱数
			Double shui = countPrict * 0.06;
			row.createCell(6).setCellFormula(String.valueOf(shui));
			row.getCell(6).setCellStyle(mapStyle.get("data_10"));

			// 税费钱数转汉子
			row.createCell(7).setCellValue("RMB" + cnFmt.format(shui));
			row.getCell(7).setCellStyle(mapStyle.get("data_11"));

			rowNum++;
			row = sheet.createRow(rowNum);
			// 项目
			row.createCell(0).setCellValue("项目金额(含税)合计");
			setColspanTitle(sheet, row, mapStyle, wb,
					"项目金额(含税)合计", "data_6",
					0, rowNum, rowNum, 0, 5);

			// 项目钱数
			Double projectMoney = shui + countPrict;
			row.createCell(6).setCellFormula(String.valueOf(projectMoney));
			row.getCell(6).setCellStyle(mapStyle.get("data_7"));

			// 项目钱数转汉子
			row.createCell(7).setCellValue("RMB" + cnFmt.format(projectMoney));
			row.getCell(7).setCellStyle(mapStyle.get("data_8"));

			rowNum++;
			row = sheet.createRow(rowNum);
			// 空
			row.createCell(0).setCellValue("");
			row.createCell(1).setCellValue("");
			row.createCell(2).setCellValue("");
			row.createCell(3).setCellValue("");
			row.createCell(4).setCellValue("");
			row.createCell(5).setCellValue("");
			setColspanTitle(sheet, row, mapStyle, wb,
					"", "data_12",
					0, rowNum, rowNum, 0, 5);
			row.createCell(6).setCellValue("高度按4米计算");
			row.getCell(6).setCellStyle(mapStyle.get("data_12"));
			row.createCell(7).setCellValue("");
			row.getCell(7).setCellStyle(mapStyle.get("data_12"));
			setColspanTitle(sheet, row, mapStyle, wb,
					"", "data_12",
					0, rowNum, rowNum, 6, 7);
		}
	}

	private void setColspanTitle(XSSFSheet sheet,XSSFRow row,
								 Map<String, XSSFCellStyle> mapStyle,
								 XSSFWorkbook wb,
								 String title,
								 String cellStyle,
								 int cell,
								 int StringRow,
								 int endRow,
								 int StringCell,
								 int endCell){
		row.createCell(cell).setCellValue(title);
		// 参数：起始行号，终止行号， 起始列号，终止列号
		CellRangeAddress region = new CellRangeAddress(StringRow,endRow,StringCell,endCell);
		sheet.addMergedRegion(region);
		row.getCell(cell).setCellStyle(mapStyle.get(cellStyle));//"data_4"
		int border = 2;
		RegionUtil.setBorderBottom(border,region, sheet, wb);
		RegionUtil.setBorderLeft(border,region, sheet, wb);
		RegionUtil.setBorderRight(border,region, sheet, wb);
		RegionUtil.setBorderTop(border,region, sheet, wb);
		row.setHeightInPoints(30);
	}
	private XSSFCellStyle createBorderedStyle(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		return style;
	}

	private Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb) {
		Map<String, XSSFCellStyle> styles = new HashMap<String, XSSFCellStyle>();
		//白色背景标题 15号粗体黑字 居左
		XSSFCellStyle style;
		Font headerFont1 = wb.createFont();
		headerFont1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont1.setFontHeightInPoints((short) 18);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(headerFont1);
		styles.put("header_1", style);
		//白色背景标题 10号粗体黑字
		Font headerFont2 = wb.createFont();
		headerFont2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont2.setFontHeightInPoints((short) 10);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(headerFont2);
		styles.put("header_2", style);
		//绿色背景标题 10号粗体黑字
		style = createBorderedStyle(wb);
		headerFont2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont2.setFontHeightInPoints((short) 10);
		style.setFillForegroundColor(IndexedColors.LIME.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(headerFont2);
		styles.put("header_3", style);

		//白色背景标题 10号粗体红字
		Font headerFont3 = wb.createFont();
		headerFont3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont3.setColor(IndexedColors.RED.getIndex());
		headerFont3.setFontHeightInPoints((short) 10);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(headerFont3);
		styles.put("header_4", style);

		//白色背景标题 15号粗体黑字 居中
		style = createBorderedStyle(wb);
		headerFont3.setFontHeightInPoints((short) 10);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(headerFont1);
		styles.put("header_5", style);

		//白色背景标题 9号粗体黑字
		Font headerFont4 = wb.createFont();
		headerFont4.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont4.setFontHeightInPoints((short) 10);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFont(headerFont4);
		styles.put("header_6", style);

		//白色背景标题 9号粗体黑字 版本标题用
		Font headerFont5 = wb.createFont();
		headerFont5.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont5.setFontHeightInPoints((short) 9);
		headerFont5.setColor(IndexedColors.RED.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFont(headerFont5);
		styles.put("header_ver", style);


		//白色背景标题 9号粗体黑字 剧中黑色背景
		Font headerFont6 = wb.createFont();
		headerFont6.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont6.setFontHeightInPoints((short) 9);
		headerFont6.setColor(IndexedColors.WHITE.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(54,54,54)));
		style.setFont(headerFont6);
		styles.put("header_7", style);


		//白色背景标题 9号粗体黑字 剧中黑色背景
		Font headerFontV7 = wb.createFont();
		headerFontV7.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFontV7.setFontHeightInPoints((short) 9);
		headerFontV7.setColor(IndexedColors.WHITE.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(54,54,54)));
		style.setFont(headerFontV7);
		styles.put("header_8", style);

		// 白色背景标题 9号黑字
		Font dataFont1 = wb.createFont();
		dataFont1.setFontHeightInPoints((short) 9);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(dataFont1);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		// 设置单元格内容垂直居中
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		styles.put("data_1", style);

		// 绿色背景标题 10号黑字
		style = createBorderedStyle(wb);
		style.setFillForegroundColor(IndexedColors.LIME.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(dataFont1);
		styles.put("data_2", style);

		//白色背景标题 10号红字
		Font dataFont2 = wb.createFont();
		dataFont2.setFontHeightInPoints((short) 10);
		dataFont2.setColor(IndexedColors.RED.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(dataFont2);
		styles.put("data_3", style);


		// 白色背景标题 9号黑字 水平 垂直居中
		Font dataFont3 = wb.createFont();
		dataFont3.setFontHeightInPoints((short) 9);
		style = createBorderedStyle(wb);
		// 设置单元格内容垂直居中
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		style.setFont(dataFont3);
		// 设置单元格内容水平剧中
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data_4", style);


		// 白色背景标题 9号黑字 水平 垂直居中 货币格式
		Font dataFont4 = wb.createFont();
		dataFont4.setFontHeightInPoints((short) 9);
		style = createBorderedStyle(wb);
		// 设置单元格内容垂直居中
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		style.setFont(dataFont4);
		// 设置单元格内容水平局右
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		XSSFDataFormat fmt = wb.createDataFormat();
		style.setDataFormat(fmt.getFormat("#,##0.00"));
		styles.put("data_5", style);

		//白色背景标题 14号粗体白字 蓝色背景 水平 垂直剧中 蓝色
		Font headerFont7 = wb.createFont();
		headerFont7.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont7.setFontHeightInPoints((short) 14);
		headerFont7.setColor(IndexedColors.WHITE.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFont(headerFont7);
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		styles.put("data_6", style);

		//白色背景标题 14号粗体白字 蓝色背景 水平 垂直剧中 蓝色
		Font headerFont8 = wb.createFont();
		headerFont8.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont8.setFontHeightInPoints((short) 14);
		headerFont8.setColor(IndexedColors.WHITE.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFont(headerFont8);
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		style.setDataFormat(fmt.getFormat("#,##0.00"));
		styles.put("data_7", style);

		//白色背景标题 14号粗体白字 蓝色背景 水平 垂直剧中 蓝色
		Font headerFont9 = wb.createFont();
		headerFont9.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont9.setFontHeightInPoints((short) 14);
		headerFont9.setColor(IndexedColors.WHITE.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFont(headerFont9);
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		styles.put("data_8", style);

		// 14号粗体白字 黄色背景 水平 垂直剧中 蓝色
		Font headerFont10 = wb.createFont();
		headerFont10.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont10.setFontHeightInPoints((short) 14);
		headerFont10.setColor(IndexedColors.BLACK.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFont(headerFont10);
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		styles.put("data_9", style);

		// 14号粗体白字 黄色背景 居右 垂直剧中 黄色
		Font headerFont11 = wb.createFont();
		headerFont11.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont11.setFontHeightInPoints((short) 14);
		headerFont11.setColor(IndexedColors.BLACK.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFont(headerFont11);
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		style.setDataFormat(fmt.getFormat("#,##0.00"));
		styles.put("data_10", style);

		// 14号粗体白字 黄色背景 居右 垂直剧中 黄色
		Font headerFont12 = wb.createFont();
		headerFont12.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont12.setFontHeightInPoints((short) 14);
		headerFont12.setColor(IndexedColors.BLACK.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//垂直
		style.setFont(headerFont12);
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND );
		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		styles.put("data_11", style);


		// 白色背景标题 10号黑字 水平 垂直居中
		Font dataFont13 = wb.createFont();
		dataFont13.setBoldweight(Font.BOLDWEIGHT_BOLD);
		dataFont13.setFontHeightInPoints((short) 10);
		style = createBorderedStyle(wb);
		// 设置单元格内容垂直居中
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		style.setFont(dataFont13);
		// 设置单元格内容水平剧中
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data_12", style);

		return styles;
	}

    private void drawingImage(Sheet newSheet, short col1, int row1,
                              short col2, int row2)
            throws IOException {

        // 文件存放路径
//        String filePath = request.getSession().getServletContext().getRealPath(File.separator);
		InputStream isLogo = getClass().getClassLoader().getResourceAsStream(LOGO_IMG);
//        File cfgFile = new InputStream(isLogo);

        // 画图
        Drawing patriarch = newSheet.createDrawingPatriarch();

        // 画图
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 150, col1, row1, col2, row2);
        anchor.setAnchorType(2);
        // 画图
//        FileInputStream fis = null;
        try {
//            fis = new FileInputStream(cfgFile);
            if (isLogo != null) {
                patriarch.createPicture(anchor,
                        newSheet.getWorkbook().addPicture(IOUtils.toByteArray(isLogo), SXSSFWorkbook.PICTURE_TYPE_JPEG));
            }
        } catch (Exception ex) {
            try {
                if (isLogo != null) {
					isLogo.close();
                }
            } catch (Exception ee) {

            }
            newSheet.getRow(row1).getCell(col1).setCellValue("失败");
        }
    }

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void createQuotation(QuotationVO quotation) {
		quotationMapper.createQuotation(quotation);
		for (int i = 0; i < quotation.getProduct().size(); i++) {
			quotation.setProductionId(quotation.getProduct().get(i).getId());
			quotation.setUnitPrice(quotation.getProduct().get(i).getUnitPrice());
			quotation.setCount(quotation.getProduct().get(i).getCount());
			quotation.setWorkContent(quotation.getProduct().get(i).getWorkContent());
			quotationMapper.createQuotationProduction(quotation);
		}
	}

	@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
	public void updateQuotation(QuotationVO quotation) {
		quotationMapper.deleteQuotationProduction(quotation.getId());
		quotationMapper.update(quotation);
		for (int i = 0; i < quotation.getProduct().size(); i++) {
			quotation.setProductionId(quotation.getProduct().get(i).getId());
			quotation.setUnitPrice(quotation.getProduct().get(i).getUnitPrice());
			quotation.setCount(quotation.getProduct().get(i).getCount());
			quotation.setWorkContent(quotation.getProduct().get(i).getWorkContent());
			quotationMapper.createQuotationProduction(quotation);
		}
	}

	public List<QuotationVO> selectQuotationList(QuotationVO quotationVO) {
		return quotationMapper.selectQuotationList(quotationVO);
	}

	public int countQuotationList(QuotationVO quotationVO) {
		return quotationMapper.countQuotationList(quotationVO);
	}


	public QuotationVO selectQuotation(String id) {
		QuotationVO quotationVO = quotationMapper.selectQuotation(id);
		ArrayList arrayList = new ArrayList();
		if (quotationVO != null) {
			quotationVO.setProjectName(projectMapper.getProjectName(quotationVO.getProjectId()));
			List<QuotationVO> list=quotationMapper.selectProduction(id);
			for(int i=0;i<list.size();i++){
				Product product = productMapper.selectProduct(list.get(i).getProductionId());
				product.setId(String.valueOf(i+1));
				product.setProduceId(list.get(i).getProductionId());
				product.setCount(list.get(i).getCount());
				product.setWorkContent(list.get(i).getWorkContent());
				arrayList.add(product);
			}

			quotationVO.setProduct(arrayList);
		}
		return quotationVO;
	}
}
