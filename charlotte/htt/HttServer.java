package charlotte.htt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import charlotte.flowertact.BlueFortewave;
import charlotte.flowertact.Fortewave;
import charlotte.satellite.MutexObject;
import charlotte.satellite.WinAPITools;
import charlotte.tools.FileTools;
import charlotte.tools.MapTools;
import charlotte.tools.ObjectList;
import charlotte.tools.StringTools;

public class HttServer {
	private static final String COMMON_ID = "{7da01163-efa3-4941-a5a6-be0800720d8e}"; // shared_uuid
	private static final String EXCL_ID = COMMON_ID + "_e";
	private static final String MUTEX_ID = COMMON_ID + "_m";
	private static final String HTT_ID = COMMON_ID + "_h";
	private static final String HTT_SERVICE_ID = COMMON_ID + "_hs";

	private static final byte[] EMPTY = new byte[0];
	private static final byte[] COMMAND_RESPONSE = new byte[] { 0x52 };
	private static final byte[] COMMAND_ERROR = new byte[] { 0x45 };

	private static MutexObject _excl = new MutexObject(EXCL_ID);
	private static MutexObject _mutex = new MutexObject(MUTEX_ID);
	private static Fortewave _pipeline;

	public static synchronized void perform(HttService service) throws Exception {
		if(service == null) {
			throw new NullPointerException("service is null");
		}
		if(_excl.waitOne(0)) {
			_mutex.waitOne(WinAPITools.INFINITE); // handled by also WHTTR.exe, Service.exe

			try {
				_pipeline = new BlueFortewave(HTT_SERVICE_ID, HTT_ID);

				while(service.interlude()) {
					Object recvData = _pipeline.recv(2000);

					if(recvData != null) {
						HttRequest req = new HttRequest((ObjectList)recvData, _pipeline);

						try {
							HttResponse res = service.service(req);

							ObjectList ol = new ObjectList();

							ol.add(COMMAND_RESPONSE);
							ol.add(((ObjectList)recvData).get(0));
							ol.add(res.getHTTPVersion().getBytes(StringTools.CHARSET_ASCII));
							ol.add(("" + res.getStatusCode()).getBytes(StringTools.CHARSET_ASCII));
							ol.add(res.getReasonPhrase().getBytes(StringTools.CHARSET_ASCII));

							{
								Map<String, String> headerFields = MapTools.<String>createIgnoreCase();

								res.writeHeaderFields(headerFields);

								headerFields.remove("Transfer-Encoding");
								headerFields.remove("Content-Length");
								headerFields.remove("Connection");

								List<String> lines = new ArrayList<String>();

								for(String key : headerFields.keySet()) {
									String value = headerFields.get(key);

									for(String div_value : StringTools.tokenize(value, "／")) {
										String div_key = key;

										for(String line_value : StringTools.tokenize(div_value, "\n")) {
											lines.add(div_key);
											lines.add(line_value);

											div_key = "";
										}
									}
								}
								ol.add(("" + (lines.size() / 2)).getBytes(StringTools.CHARSET_ASCII));

								for(String line : lines) {
									ol.add(line.getBytes(StringTools.CHARSET_ASCII));
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
										byte[] bodyPart = res.getBodyPart();

										if(bodyPart == null) {
											bodyPart = EMPTY;
										}
										ol.add(bodyPart);
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
						finally {
							FileTools.del(req.getHeaderPartFile());
							FileTools.del(req.getBodyPartFile());
						}
					}
				}
			}
			finally {
				if(_pipeline != null) {
					_pipeline.close();
					_pipeline = null;
				}
				_mutex.release();
				_excl.release();
			}
		}
	}
}
