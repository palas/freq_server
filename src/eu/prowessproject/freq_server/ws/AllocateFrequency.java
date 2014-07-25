/**
 * Copyright (c) 2014, Pablo Lamela Seijas
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Created: 2014-05-25
 */
package eu.prowessproject.freq_server.ws;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import eu.prowessproject.freq_server.messages.constants.Constants;
import eu.prowessproject.freq_server.messages.response.FreqServerResponse;
import eu.prowessproject.freq_server.messages.response.ObjectFactory;
import eu.prowessproject.freq_server.messages.response.Result;
import eu.prowessproject.freq_server.state.FreqServerState;
import eu.prowessproject.freq_server.state.exceptions.NoFrequenciesAvailable;
import eu.prowessproject.freq_server.state.exceptions.NotRunning;
import eu.prowessproject.freq_server.utils.Utils;

/**
 * Servlet implementation class AllocateFrequency
 */
@WebServlet("/AllocateFrequency")
public class AllocateFrequency extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllocateFrequency() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FreqServerState state = FreqServerState.getFreqServerStateFromRequest(request);
		// Response
		ObjectFactory of = new ObjectFactory();
		FreqServerResponse responsePacket = of.createFreqServerResponse();
		try {
			Result result = new Result();
			result.setFrequencyAllocated(state.allocate());
			responsePacket.setResult(result);
			responsePacket.setState(Constants.OK);
		} catch (NoFrequenciesAvailable e) {
			Utils.setError(responsePacket, Constants.NO_FREQUENCIES_AVAILABLE);
		} catch (NotRunning e) {
			Utils.setError(responsePacket, Constants.NOT_RUNNING);
		}
		JAXB.marshal(responsePacket, response.getOutputStream());
		Utils.disableCache(response);
	}

}
