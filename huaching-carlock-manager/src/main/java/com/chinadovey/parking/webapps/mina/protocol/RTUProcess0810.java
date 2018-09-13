package com.chinadovey.parking.webapps.mina.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.mina.proxy.utils.ByteUtilities;

import com.chinadovey.parking.webapps.mina.RTUProcess;
import com.chinadovey.parking.webapps.mina.pojo.Equipment0810;

public class RTUProcess0810 implements RTUProcess {

	@Override
	public List<Equipment0810> process(RTUPackets rtuPackets) {

		rtuPackets.getValue();

		List<Equipment0810> list = new ArrayList<Equipment0810>();
		Equipment0810 data;
		int i = 0;
		while (i < rtuPackets.getValue().length) {
			data = new Equipment0810();
			data.setId(UUID.randomUUID().toString());
			data.setType(rtuPackets.getValue()[i++]);
			data.setRtuId(ByteUtilities.asHex(rtuPackets.getRtuId()));
			data.setEquiId(ByteUtilities.asHex(Arrays.copyOfRange(
					rtuPackets.getValue(), i, i += 2)));
			data.setEquiIdbytes(Arrays.copyOfRange(rtuPackets.getValue(),
					i - 2, i));
			data.setRtuIdbytes(rtuPackets.getRtuId());
			switch (data.getType()) {
			case 0x00:
				float f = Float.intBitsToFloat(ByteUtilities.makeIntFromByte4(
						rtuPackets.getValue(), i));
				data.setValue((float)(Math.round(f*100))/100);
				i += 4;
				break;
			case 0x01:
				data.setStatus(rtuPackets.getValue()[i++]);
				break;
			case 0x02:
				data.setStatus(rtuPackets.getValue()[i++]);

				f = Float.intBitsToFloat(ByteUtilities.makeIntFromByte4(
						rtuPackets.getValue(), i));
				data.setValue((float)(Math.round(f*100))/100);
				i += 4;
				break;
			}
			list.add(data);
		}

		return list;
	}
}
