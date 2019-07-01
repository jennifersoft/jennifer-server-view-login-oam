package com.aries.oam.adapter.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

public class OAMUtil {

    /*
    '--------------------------------------------------------------------
    ' Http Header 를 Map 에 추가
    ' Emergency 상태일경우 json data 를 parsing 하여 Map 에 추가
    '--------------------------------------------------------------------
    */
    public static void httpHeaderToMap(HttpServletRequest req, Map<String, String> map )
    {
        Base64 decoder = new Base64() ;

        // Header 의 모든 값들을 추출 하여 Map 에 담는다.
        Enumeration<String> enames;
        enames = req.getHeaderNames(); //

        while (enames.hasMoreElements())
        {
            String key = (String) enames.nextElement()  ;
            String val = req.getHeader(key);

            if ( val.startsWith( "=?UTF-8?B?" ) )
            {
                val = convData( decoder, val ) ;
            }

            // map 에 등록
            map.put(key, val ) ;
        }


        String emerFlag = req.getParameter( "OAM_EMER_FLAG" ) ;

        if( "TRUE".equalsIgnoreCase( emerFlag ) )
        {
            jsonToMap( req, map ) ;
        }
    }


    /*-----------------------------------------------------------------
    ' json 데이터를 parsing 하여 Map 에 추가
    ' 본 sample 에서는 가장 단순한 형태의 json 만 parsing 가능하므로
    ' 각 업무팀 별로 사용하는 JSON Parser 사용을 권고함
    '----------------------------------------------------------------- */
    public static void jsonToMap(HttpServletRequest req, Map<String, String> map )
    {
        Base64 decoder = new Base64() ;

        String emerData = req.getParameter("OAM_EMER_DATA");
        if( emerData == null || emerData.equals("") )
        {
            return ;
        }

        String sTmp = emerData.replaceAll("[{}\"]*", "");
        String[] sArr = sTmp.split(",");
        String key = "";
        String val = "";
        int idx=0;
        for (String sData : sArr){
            String[] arrTmp = sData.split(":");
            for(String sItem : arrTmp){
                if(idx==0){
                    key = sItem;
                    idx++;
                }else{
                    val = sItem;
                    idx=0;
                }
            }

            if ( val.startsWith( "=?UTF-8?B?" ) )
            {
                convData( decoder, val ) ;
            }

            // key를 map 에 등록
            map.put( key, val ) ;

            key = "";
            val = "";
            idx=0;
        }
    }

    /*
    '----------------------------------------
    ' allowed url 목록
    ' 업무시스템에서 기존 사용 로직이 있을 경우 기존 사용 로직을 권고함
    ' 기존 로직이 없을경우 아래 sample을 참고하여 반드시 적용 해야 함
    '----------------------------------------
    */
    private static final String[] ALLOWED_REFER_URL = {
            "oam.samsunglife.kr:0000"
            ,	"oam.samsunglife.kr"
            ,	"ida.samsunglife.kr"
            ,	"app.samsunglife.kr"
            ,	"wb1.t.ida.samsunglife.kr"
            ,	"localhost"
    } ;

    public static boolean checkRefer( String refUrl )
    {
        if( refUrl == null || refUrl.trim().length() <= 0 )
        {
            return false ;
        }

        for( String allowed_url : ALLOWED_REFER_URL )
        {
            if( refUrl.indexOf( allowed_url ) > 0 )
            {
                return true ;
            }
        }

        return false;
    }

    public static String convData( Base64 decoder, String val )
    {
        StringBuffer buf = new StringBuffer() ;

        String[] ar = val.split( " " ) ;

        for( int i = 0 ; i < ar.length ; i++ )
        {
            String tmpVal = ar[i] ;

            if ( tmpVal.startsWith( "=?UTF-8?B?" ) && tmpVal.endsWith( "?=" ))
            {
                if( buf.toString().length() > 0 )
                {
                    buf.append( " " ) ;
                }

                String base64str = tmpVal.substring( 10, tmpVal.length() - 2 ) ;

                byte[] bytes = decoder.decode( base64str ) ;

                try
                {
                    tmpVal = new String( bytes, "UTF-8" ) ;
                }
                catch( Throwable t )
                {
                    tmpVal = new String( bytes ) ;
                }

                buf.append( tmpVal ) ;
            }
        }

        return buf.toString() ;
    }
}

class Base64
{
    public Base64()
    {
        super();
    }

    /**
     *
     * Encode some data and return a String.
     *
     */
    public String encode(byte[] d)
    {
        if (d == null)
        {
            return null;
        }

        byte data[] = new byte[d.length + 2];

        System.arraycopy(d, 0, data, 0, d.length);

        byte dest[] = new byte[(data.length / 3) * 4];

        // 3-byte to 4-byte conversion

        for (int sidx = 0, didx = 0; sidx < d.length; sidx += 3, didx += 4)
        {
            dest[didx] = (byte) ((data[sidx] >>> 2) & 077);

            dest[didx + 1] = (byte) ((data[sidx + 1] >>> 4) & 017 | (data[sidx] << 4) & 077);

            dest[didx + 2] = (byte) ((data[sidx + 2] >>> 6) & 003 |	(data[sidx + 1] << 2) & 077);

            dest[didx + 3] = (byte) (data[sidx + 2] & 077);
        }

        // 0-63 to ascii printable conversion
        for (int idx = 0; idx < dest.length; idx++)
        {
            if (dest[idx] < 26)
            {
                dest[idx] = (byte) (dest[idx] + 'A');
            }
            else if (dest[idx] < 52)
            {
                dest[idx] = (byte) (dest[idx] + 'a' - 26);
            }
            else if (dest[idx] < 62)
            {
                dest[idx] = (byte) (dest[idx] + '0' - 52);
            }
            else if (dest[idx] < 63)
            {
                dest[idx] = (byte) '+';
            }
            else
            {
                dest[idx] = (byte) '/';
            }

        }

        // add padding
        for (int idx = dest.length - 1; idx > (d.length * 4) / 3; idx--)
        {
            dest[idx] = (byte) '=';
        }

        return new String(dest);
    }

    /**
     *
     * Encode a String using Base64 using the default platform encoding
     *
     **/
    public String encode(String s)
    {
        return encode(s.getBytes());
    }

    /**
     *
     * Decode data and return bytes.
     *
     */
    public byte[] decode(String str)
    {
        if (str == null)
        {
            return null;
        }

        byte data[] = str.getBytes();

        return decode(data);
    }

    /**
     *
     * Decode data and return bytes. Assumes that the data passed
     *
     * in is ASCII text.
     *
     */
    public byte[] decode(byte[] data)
    {
        int tail = data.length;
        while (data[tail - 1] == '=')
        {
            tail--;
        }

        byte dest[] = new byte[tail - data.length / 4];

        // ascii printable to 0-63 conversion
        for (int idx = 0; idx < data.length; idx++)
        {
            if (data[idx] == '=')
            {
                data[idx] = 0;
            }
            else if (data[idx] == '/')
            {
                data[idx] = 63;
            }
            else if (data[idx] == '+')
            {
                data[idx] = 62;
            }
            else if (data[idx] >= '0' && data[idx] <= '9')
            {
                data[idx] = (byte) (data[idx] - ('0' - 52));
            }
            else if (data[idx] >= 'a' && data[idx] <= 'z')
            {
                data[idx] = (byte) (data[idx] - ('a' - 26));
            }
            else if (data[idx] >= 'A' && data[idx] <= 'Z')
            {
                data[idx] = (byte) (data[idx] - 'A');
            }

        }

        // 4-byte to 3-byte conversion
        int sidx, didx;

        for (sidx = 0, didx = 0; didx < dest.length - 2; sidx += 4, didx += 3)
        {

            dest[didx] = (byte) (((data[sidx] << 2) & 255) | ((data[sidx + 1] >>> 4) & 3));

            dest[didx + 1] = (byte) (((data[sidx + 1] << 4) & 255) | ((data[sidx + 2] >>> 2) & 017));

            dest[didx + 2] = (byte) (((data[sidx + 2] << 6) & 255) | (data[sidx + 3] & 077));

        }

        if (didx < dest.length)
        {
            dest[didx] = (byte) (((data[sidx] << 2) & 255) | ((data[sidx + 1] >>> 4) & 3));
        }

        if (++didx < dest.length)
        {
            dest[didx] = (byte) (((data[sidx + 1] << 4) & 255) | ((data[sidx + 2] >>> 2) & 017));
        }

        return dest;

    }
}