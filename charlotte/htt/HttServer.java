package charlotte.htt;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import charlotte.flowertact.Fortewave;
import charlotte.satellite.MutexObject;
import charlotte.satellite.ObjectList;
import charlotte.satellite.WinAPITools;
import charlotte.tools.ByteBuffer;
import charlotte.tools.StringTools;

public class HttServer {
	private static final String COMMON_ID = "{7da01163-efa3-4941-a5a6-be0800720d8e}";
	private static final String MUTEX_ID = COMMON_ID + "_m";
	private static final String HTT_ID = COMMON_ID + "_h";
	private static final String HTT_SERVICE_ID = COMMON_ID + "_hs";

	private static final byte[] EMPTY = new byte[0];
	private static final byte[] COMMAND_CLEAR = new byte[] { 0x43 };
	private static final byte[] COMMAND_RESPONSE = new byte[] { 0x52 };
	private static final byte[] COMMAND_ERROR = new byte[] { 0x45 };

	private static MutexObject _mutex = new MutexObject(MUTEX_ID);
	private static Fortewave _pipeline;

	public static synchronized void perform(HttService service) throws Exception {
		if(service == null) {
			throw new NullPointerException("service is null");
		}
		if(WinAPITools.existWinAPIToolsFile() == false) {
			throw new Exception("WinAPITools.exe is not exist");
		}
		if(_mutex.waitOne(0)) {
			try {
				_pipeline = new Fortewave(HTT_SERVICE_ID, HTT_ID);
				_pipeline.send(new ObjectList(COMMAND_CLEAR));

				while(service.interlude()) {
					Object recvData = _pipeline.recv(2000);

					if(recvData != null) {
						HttResponse res = service.service(new HttRequest((ObjectList)recvData));

						try {
							ObjectList ol = new ObjectList();

							ol.add(COMMAND_RESPONSE);
							ol.add(((ObjectList)recvData).get(0));
							ol.add(res.getHTTPVersion().getBytes(StringTools.CHARSET_ASCII));
							ol.add(("" + res.getStatusCode()).getBytes(StringTools.CHARSET_ASCII));
							ol.add(res.getReasonPhrase().getBytes(StringTools.CHARSET_ASCII));

							{
								Map<String, String> headerFields = new HashMap<String, String>();

								res.writeHeaderFields(headerFields);

								ol.add(("" + headerFields.keySet().size()).getBytes(StringTools.CHARSET_ASCII));

								for(String key : headerFields.keySet()) {
									String value = headerFields.get(key);

									ol.add(key.getBytes(StringTools.CHARSET_ASCII));
									ol.add(value.getBytes(StringTools.CHARSET_ASCII));
								}
							}

							{
								File bodyPartFile = res.getBodyPartFile();

								if(bodyPartFile != null) {
									ol.add(bodyPartFile.getCanonicalPath().getBytes(StringTools.CHARSET_SJIS));
									ol.add(EMPTY);
								}
								else {
									ol.add(EMPTY);

									{
										ByteBuffer bodyPartBuff = new ByteBuffer();

										res.writeBodyPart(bodyPartBuff);

										ol.add(bodyPartBuff.getBytes());
									}
								}
							}

							_pipeline.send(ol);
						}
						catch(Throwable e) {
							e.printStackTrace();

							_pipeline.send(new ObjectList(
									COMMAND_ERROR,
									((ObjectList)recvData).get(0)
									));
						}
					}
				}
			}
			finally {
				if(_pipeline != null) {
					_pipeline.send(new ObjectList(COMMAND_CLEAR));
					_pipeline.close();
					_pipeline = null;
				}
				_mutex.release();
			}
		}
	}
}
