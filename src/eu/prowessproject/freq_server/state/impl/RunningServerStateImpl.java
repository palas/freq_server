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
package eu.prowessproject.freq_server.state.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import eu.prowessproject.freq_server.state.IFreqServerStateImpl;
import eu.prowessproject.freq_server.state.exceptions.AlreadyStarted;
import eu.prowessproject.freq_server.state.exceptions.NoFrequenciesAvailable;
import eu.prowessproject.freq_server.state.exceptions.NotAllocated;
import eu.prowessproject.freq_server.state.exceptions.NotRunning;

public class RunningServerStateImpl extends GenericServerImpl implements
		IFreqServerStateImpl {

	private Queue<Integer> freeFrequencies = new LinkedList<Integer>();
	private HashSet<Integer> allocatedFrequencies = new HashSet<Integer>();

	RunningServerStateImpl() {
		for (int freq : INITIAL_FREQUENCIES) {
			freeFrequencies.add(freq);
		}
	}

	@Override
	public void start_impl() throws AlreadyStarted {
		throw new AlreadyStarted();
	}

	@Override
	public void stop_impl() throws NotRunning {
		this.changeState(new StoppedServerStateImpl());
	}

	@Override
	public Integer allocate_impl() throws NoFrequenciesAvailable, NotRunning {
		try {
			Integer allocatedFreq = freeFrequencies.remove();
			allocatedFrequencies.add(allocatedFreq);
			return allocatedFreq;
		} catch (NoSuchElementException e) {
			throw new NoFrequenciesAvailable();
		}
	}

	@Override
	public void deallocate_impl(Integer frequency) throws NotAllocated, NotRunning {
		if (allocatedFrequencies.contains(frequency)) {
			allocatedFrequencies.remove(frequency);
			freeFrequencies.add(frequency);
		} else {
			throw new NotAllocated();
		}
	}

}
