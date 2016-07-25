package net.latin.client.widget.msg;


public class GwtMsgFormat {

	private GwtMsgFormat() {
	}

	public static String getMsg( String pattern, Object param ) {
		return getMsg( pattern, new Object[]{ param } );
	}

	public static String getMsg( String pattern, Object param1, Object param2 ) {
		return getMsg( pattern, new Object[]{ param1, param2 } );
	}

	public static String getMsg( String pattern, Object param1, Object param2, Object param3 ) {
		return getMsg( pattern, new Object[]{ param1, param2, param3 } );
	}

	public static String getMsg(String pattern ,Object[] objects) {
	//	this.applyPattern(this.pattern);
		StringBuffer ret = new StringBuffer();
		StringBuffer[] segments = new StringBuffer[4];
		   for (int i = 0; i < segments.length; ++i) {
	          segments[i] = new StringBuffer();
	      }
	      int part = 0;
	      int formatNumber = 0;
	      boolean inQuote = false;
	      int braceStack = 0;
	     // maxOffset = -1;
	      for (int i = 0; i < pattern.length(); ++i) {
	          char ch = pattern.charAt(i);
	          if (part == 0) {
	              if (ch == '\'') {
	                  if (i + 1 < pattern.length()
	                      && pattern.charAt(i+1) == '\'') {
	                      segments[part].append(ch);  // handle doubles
	                      ret.append(ch);
	                      ++i;
	                  } else {
	                      inQuote = !inQuote;
	                  }
	              } else if (ch == '{' && !inQuote) {
	                  part = 1;
	              } else {
	                  segments[part].append(ch);
	                  ret.append(ch);
	              }
	          } else  if (inQuote) {              // just copy quotes in parts
	              segments[part].append(ch);
	              if (ch == '\'') {
	                  inQuote = false;
	              }
	          } else {
	              switch (ch) {
		              case '{':
		                  ++braceStack;
		                  segments[part].append(ch);
		                  ret.append(ch);
		                  break;
		              case '}':
		                  if (braceStack == 0) {
		                      part = 0;
		                      ret.append(makeFormat(i, formatNumber, segments,objects));
		                      formatNumber++;
		                  } else {
		                      --braceStack;
		                      segments[part].append(ch);
		                      ret.append(ch);
		                  }
		                  break;
		              case '\'':
		                  inQuote = true;
		                  // fall through, so we keep quotes in other parts
		              default:
		                  segments[part].append(ch);
		              	  if(part == 0){
		              		  ret.append(ch);
		              	  }
		                  break;
		              }
	          }
	      }
	      if (braceStack == 0 && part != 0) {
	          throw new IllegalArgumentException("Unmatched braces in the pattern.");
	      }
		return ret.toString();
	}

	private static String makeFormat(int position, int offsetNumber, StringBuffer[] segments, Object[] objects) {
		Integer posObj = new Integer(segments[1].toString());
		segments[1].setLength(0);
		if(posObj.intValue() < objects.length){
			return objects[posObj.intValue()].toString();
		}
		return "{" + posObj.intValue() + "}";
	}
}
