<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:altova="http://www.altova.com">
	<xsl:output version="4.0" method="html" indent="no" encoding="UTF-8" doctype-public="-//W3C//DTD HTML 4.0 Transitional//EN" doctype-system="http://www.w3.org/TR/html4/loose.dtd"/>
	<xsl:param name="SV_OutputFormat" select="'HTML'"/>
<xsl:param name="fecha"/>
<xsl:variable name="pages" select="pages"/>
<xsl:template match="bean">
		<html>
			<head>
				<title/>
				<style type="text/css">
					<xsl:comment>P.MsoNormal { font-family:times new roman;
font-size:12pt;
margin:0cm 0cm 0pt;
mso-fareast-font-family:times new roman;
mso-pagination:widow-orphan;
 }
 LI.MsoNormal { font-family:times new roman;
font-size:12pt;
margin:0cm 0cm 0pt;
mso-fareast-font-family:times new roman;
mso-pagination:widow-orphan;
 }
 DIV.MsoNormal { font-family:times new roman;
font-size:12pt;
margin:0cm 0cm 0pt;
mso-fareast-font-family:times new roman;
mso-pagination:widow-orphan;
 }
 P.MsoBodyTextIndent { font-family:times new roman;
font-size:12pt;
margin:0cm 0cm 0pt 18pt;
mso-fareast-font-family:arial unicode ms;
mso-pagination:widow-orphan;
 }
 LI.MsoBodyTextIndent { font-family:times new roman;
font-size:12pt;
margin:0cm 0cm 0pt 18pt;
mso-fareast-font-family:arial unicode ms;
mso-pagination:widow-orphan;
 }
 DIV.MsoBodyTextIndent { font-family:times new roman;
font-size:12pt;
margin:0cm 0cm 0pt 18pt;
mso-fareast-font-family:arial unicode ms;
mso-pagination:widow-orphan;
 }
 P { font-family:arial unicode ms;
font-size:12pt;
margin-left:0cm;
margin-right:0cm;
mso-margin-bottom-alt:auto;
mso-margin-top-alt:auto;
mso-pagination:widow-orphan;
 }
 SPAN.GramE { mso-gram-e:yes;
 }</xsl:comment>
				</style>
			</head>
			<body>
					<div class="Section1">
						<p class="MsoNormal">
							<span style="font-size:14pt; font-weight:bold; text-decoration:underline; ">
								<xsl:text>Especificación:</xsl:text>
							</span>
							<p style = "font-weight:bold; text-decoration:underline; "/>
						</p>
						<p class="MsoNormal">
							<p>
								<span>
									<xsl:text> </xsl:text>
								</span>
							</p>
						</p>
						<p class="MsoNormal">
							<p>
								<span>
									<xsl:text> </xsl:text>
								</span>
							</p>
						</p>
						<table style="border-bottom:medium none; border-collapse:collapse; border-left:medium none; border-right:medium none; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-insideh:.5pt solid windowtext; mso-border-insidev:.5pt solid windowtext; mso-padding-alt:0cm 3.5pt 0cm 3.5pt; " border="1" cellPadding="0" cellSpacing="0" class="MsoNormalTable">
							<tbody>
								<tr style="mso-yfti-firstrow:yes; mso-yfti-irow:0; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:windowtext 1pt solid; mso-border-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:147.5pt; " vAlign="top" width="197">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Nombre del caso:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
									</td>
									<td style="border-bottom:windowtext 1pt solid; border-left:medium none; border-right:windowtext 1pt solid; border-top:windowtext 1pt solid; mso-border-alt:solid windowtext.5pt; mso-border-left-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:243pt; " vAlign="top" width="324">
										<p class="MsoNormal">
											<span style="mso-spacerun:yes; ">
												<xsl:text> </xsl:text>
											</span>
											<span style="font-size:11pt; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:value-of select="useCaseName"/>
											</span>
											<p/>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:1; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:147.5pt; " vAlign="top" width="197">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Autor:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
									</td>
									<td style="border-bottom:windowtext 1pt solid; border-left:medium none; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-left-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:243pt; " vAlign="top" width="324">
										<p class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:2; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:147.5pt; " vAlign="top" width="197">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Fecha de creación:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
									</td>
									<td style="border-bottom:windowtext 1pt solid; border-left:medium none; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-left-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:243pt; " vAlign="top" width="324">
										<p style="margin:0cm 0cm 0pt; ">
											<span style="mso-spacerun:yes; ">
												<xsl:text> </xsl:text>
											</span>
											<span style="font-family:times new roman; font-size:11pt; mso-bidi-font-size:12.0pt; ">
												<xsl:value-of select="$fecha"/>
											</span>
											<p/>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:3; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:147.5pt; " vAlign="top" width="197">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Entrada menú:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
									</td>
									<td style="border-bottom:windowtext 1pt solid; border-left:medium none; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-left-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:243pt; " vAlign="top" width="324">
										<p style="margin:0cm 0cm 0pt; ">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:4; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " colSpan="2" vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Descripción:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
										<p class="MsoNormal">
											<p style="font-weight:bold; ">
												<span style="font-weight:bold; ">
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p class="MsoNormal">
											<p style="font-weight:bold; ">
												<span style="font-weight:bold; ">
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:5; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " colSpan="2" vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Actores: </xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
										<p class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:6; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " colSpan="2" vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Precondiciones:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
										<p class="MsoNormal">
											<p style="font-weight:bold; ">
												<span style="font-weight:bold; ">
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p style="margin:0cm 0cm 0pt; ">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:7; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " colSpan="2" vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Flujo Normal:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
										<p class="MsoNormal">
											<p style="font-weight:bold; ">
												<span style="font-weight:bold; ">
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p style="margin-left:18pt; " class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:8; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " colSpan="2" vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Flujo Alternativo:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
										<p style="margin-left:18pt; " class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p style="margin-left:18pt; " class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p style="margin-left:18pt; " class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:9; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " colSpan="2" vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Poscondiciones:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
										<p style="margin:0cm 0cm 0pt; ">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p style="margin:0cm 0cm 0pt; ">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:10; mso-yfti-lastrow:yes; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " colSpan="2" vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Casos de uso relacionados:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
										<p class="MsoNormal">
											<p style="font-weight:bold; ">
												<span style="font-weight:bold; ">
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p class="MsoNormal">
											<p style="font-weight:bold; ">
												<span style="font-weight:bold; ">
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
							</tbody>
						</table>
						<p class="MsoNormal">
							<p>
								<span>
									<xsl:text> </xsl:text>
								</span>
							</p>
						</p>
						<p class="MsoNormal">
							<span style="font-size:14pt; font-weight:bold; text-decoration:underline; ">
								<xsl:text>Elementos de persistencia:</xsl:text>
							</span>
							<p style="font-weight:bold; text-decoration:underline; "/>
						</p>
						<p class="MsoNormal">
							<p style="font-weight:bold; text-decoration:underline; ">
								<span style="font-weight:bold; text-decoration:none; ">
									<xsl:text> </xsl:text>
								</span>
							</p>
						</p>
						<table style="border-bottom:medium none; border-collapse:collapse; border-left:medium none; border-right:medium none; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-insideh:.5pt solid windowtext; mso-border-insidev:.5pt solid windowtext; mso-padding-alt:0cm 3.5pt 0cm 3.5pt; " border="1" cellPadding="0" cellSpacing="0" class="MsoNormalTable">
							<tbody>
								<tr style="mso-yfti-firstrow:yes; mso-yfti-irow:0; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:windowtext 1pt solid; mso-border-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Tablas:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
										<p class="MsoNormal">
											<p style="font-weight:bold; ">
												<span style="font-weight:bold; ">
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
										<p class="MsoNormal">
											<p>
												<span>
													<xsl:text> </xsl:text>
												</span>
											</p>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:1; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Vistas:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:2; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Stored Procedures:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
									</td>
								</tr>
								<tr style="mso-yfti-irow:3; mso-yfti-lastrow:yes; ">
									<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " vAlign="top" width="521">
										<p class="MsoNormal">
											<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
												<xsl:text>Otros:</xsl:text>
											</span>
											<p style="font-weight:bold; "/>
										</p>
									</td>
								</tr>
							</tbody>
						</table>
						<p class="MsoNormal">
							<p style="font-weight:bold; text-decoration:underline; ">
								<span style="font-weight:bold; text-decoration:none; ">
									<xsl:text> </xsl:text>
								</span>
							</p>
						</p>
						<p class="MsoNormal">
							<p>
								<span>
									<xsl:text> </xsl:text>
								</span>
							</p>
						</p>
						<p class="MsoNormal">
							<span style="font-size:14pt; font-weight:bold; text-decoration:underline; ">
								<xsl:text>Elementos de interfaz de usuario:</xsl:text>
							</span>
							<p style="font-weight:bold; text-decoration:underline; "/>
						</p>
						<p class="MsoNormal">
							<p>
								<span>
									<xsl:text> </xsl:text>
								</span>
							</p>
						</p>

						<xsl:for-each select="pageName">
							<table style="border-bottom:medium none; border-collapse:collapse; border-left:medium none; border-right:medium none; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-insideh:.5pt solid windowtext; mso-border-insidev:.5pt solid windowtext; mso-padding-alt:0cm 3.5pt 0cm 3.5pt; " border="1" cellPadding="0" cellSpacing="0" class="MsoNormalTable">
								<tbody>
									<tr style="mso-yfti-firstrow:yes; mso-yfti-irow:0; ">
										<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:windowtext 1pt solid; mso-border-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:147.5pt; " vAlign="top" width="197">
											<p class="MsoNormal">
												<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
													<xsl:text>Ventana:</xsl:text>
												</span>
												<p style="font-weight:bold; "/>
											</p>
										</td>
										<td style="border-bottom:windowtext 1pt solid; border-left:medium none; border-right:windowtext 1pt solid; border-top:windowtext 1pt solid; mso-border-alt:solid windowtext.5pt; mso-border-left-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:243pt; " vAlign="top" width="324">
											<p class="MsoNormal">
												<span style="font-size:11pt; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
													<xsl:value-of select="."/>
												</span>
												<p/>
											</p>
										</td>
									</tr>
									<tr style="mso-yfti-irow:1; ">
										<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:147.5pt; " vAlign="top" width="197">
											<p class="MsoNormal">
												<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
													<xsl:text>Descripción:</xsl:text>
												</span>
												<p style="font-weight:bold; "/>
											</p>
										</td>
										<td style="border-bottom:windowtext 1pt solid; border-left:medium none; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-left-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:243pt; " vAlign="top" width="324">
											<p class="MsoNormal">
												<p>
													<span>
														<xsl:text> </xsl:text>
													</span>
												</p>
											</p>
										</td>
									</tr>
									<tr style="mso-yfti-irow:2; mso-yfti-lastrow:yes; ">
										<td style="border-bottom:windowtext 1pt solid; border-left:windowtext 1pt solid; border-right:windowtext 1pt solid; border-top:medium none; mso-border-alt:solid windowtext.5pt; mso-border-top-alt:solid windowtext.5pt; padding-bottom:0cm; padding-left:3.5pt; padding-right:3.5pt; padding-top:0cm; width:390.5pt; " colSpan="2" vAlign="top" width="521">
											<p class="MsoNormal">
												<span style="font-size:11pt; font-weight:bold; mso-bidi-font-size:12.0pt; mso-fareast-font-family:arial unicode ms; ">
													<xsl:text>Navegabilidad:</xsl:text>
												</span>
												<p style="font-weight:bold; "/>
											</p>
											<p style="margin-left:18pt; " class="MsoNormal">
												<span style="font-weight:bold; mso-spacerun:yes; ">
													<xsl:text>     </xsl:text>
												</span>
												<p style="font-weight:bold; "/>
											</p>
											<p class="MsoNormal">
												<p>
													<span>
														<xsl:text> </xsl:text>
													</span>
												</p>
											</p>
										</td>
									</tr>
								</tbody>
							</table>
							<p class="MsoNormal">
								<p>
									<span>
										<xsl:text> </xsl:text>
									</span>
								</p>
							</p>
						</xsl:for-each>
					</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
