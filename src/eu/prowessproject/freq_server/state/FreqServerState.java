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
package eu.prowessproject.freq_server.state;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import eu.prowessproject.freq_server.state.exceptions.AlreadyStarted;
import eu.prowessproject.freq_server.state.exceptions.NoFrequenciesAvailable;
import eu.prowessproject.freq_server.state.exceptions.NotAllocated;
import eu.prowessproject.freq_server.state.exceptions.NotRunning;
import eu.prowessproject.freq_server.state.impl.GenericServerImpl;
import eu.prowessproject.freq_server.state.impl.StoppedServerStateImpl;

public class FreqServerState implements IFreqServerImpl {

	private static final String CONTEXT_ATRIBUTE_NAME = "FREQ_SERVER_STATE_OBJECT";

	public static synchronized void initialiseServerState(
			ServletContext servletContext) {
		if (servletContext.getAttribute(CONTEXT_ATRIBUTE_NAME) != null) {
			throw (new RuntimeException(
					"State already initialised in this context!"));
		} else {
			servletContext.setAttribute(CONTEXT_ATRIBUTE_NAME, new FreqServerState());
		}
	}

	public static FreqServerState getFreqServerStateFromRequest(
			HttpServletRequest request) {
		Object rawFreqServerState = request.getServletContext().getAttribute(CONTEXT_ATRIBUTE_NAME);
		if (rawFreqServerState != null) {
			if (rawFreqServerState instanceof FreqServerState) {
				return (FreqServerState)rawFreqServerState;
			} else {
				throw (new RuntimeException("Wrong object when retrieving the "
						+ FreqServerState.class.getSimpleName()));
			}
		} else {
			throw (new RuntimeException("Could not retrieve the "
					 + FreqServerState.class.getSimpleName()));
		}
	}

	private IFreqServerImpl state;

	public FreqServerState() {
		this.state = new GenericServerImpl(new StoppedServerStateImpl());
	}

	@Override
	public synchronized void start() throws AlreadyStarted {
		this.state.start();
	}

	@Override
	public synchronized void stop() throws NotRunning {
		this.state.stop();
	}

	@Override
	public synchronized Integer allocate() throws NoFrequenciesAvailable, NotRunning {
		return this.state.allocate();
	}

	@Override
	public synchronized void deallocate(Integer frequency) throws NotAllocated, NotRunning {
		this.state.deallocate(frequency);
	}
	
}
