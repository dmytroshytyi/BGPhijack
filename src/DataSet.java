import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;



public class DataSet {
		
	//ping for X times per time step combine into 1 mean result.
	public static ArrayList<Monitor> makeMeanValuesOfMesurement(ArrayList<Monitor> monitors){
		ArrayList<Monitor> tmpmonitors = new ArrayList<Monitor>();
		int numMonitor = -1;
		for (Monitor each: monitors){
			boolean matched = false;
			for (Monitor tmpm: tmpmonitors){
				if(each.getHOST().equals(tmpm.getHOST()) //)
				&&(each.getTimeStep()==tmpm.getTimeStep()))
				{
					matched = true;
					// THIS IS ERROR. FIX IT!!!
						tmpmonitors.get(numMonitor).setTTL( (each.getTTL()+ tmpmonitors.get(numMonitor).getTTL())/2 ); 
						tmpmonitors.get(numMonitor).setRTT( (each.getRTT()+ tmpmonitors.get(numMonitor).getRTT())/2 );
					// THIS IS WRONG COMPUTATION OF AVARAGE!!!!
				}
			}
			if (matched == false){
				tmpmonitors.add(each);
				numMonitor++;
			}
		}
		return tmpmonitors; 
	}
	
	//This function will help you
	
	public static ArrayList<Monitor> makeIntervalValues(ArrayList<Monitor> monitors){
		ArrayList<Monitor> tmpmonitors = new ArrayList<Monitor>();
		int numMonitor = -1;
		for (Monitor each: monitors){
			boolean matched = false;
			for (Monitor tmpm: tmpmonitors){
				if(each.getHOST().equals(tmpm.getHOST()) //)
				&&(each.getTimeStep()==tmpm.getTimeStep()))
				{
					matched = true;
					// THIS IS ERROR. FIX IT!!!
						tmpmonitors.get(numMonitor).setTTL( (each.getTTL()+ tmpmonitors.get(numMonitor).getTTL())/2 ); 
						tmpmonitors.get(numMonitor).setRTT( (each.getRTT()+ tmpmonitors.get(numMonitor).getRTT())/2 );
					// THIS IS WRONG COMPUTATION OF AVARAGE!!!!
				}
			}
			if (matched == false){
				tmpmonitors.add(each);
				numMonitor++;
			}
		}
		return tmpmonitors; 
	}
	
	public static ArrayList<ArrayList<Monitor>> timeStepsFor(ArrayList<Monitor> listMonitors){
		int currentTimeStep = 1;
		
		ArrayList<ArrayList<Monitor>> timeSteps = new ArrayList<ArrayList<Monitor>>() ;
		ArrayList<Monitor> tmpMonitorSetForOneTimeStep = new ArrayList<Monitor>() ;
		
		
		for (Monitor monitor: listMonitors){
			if (monitor.getTimeStep() == currentTimeStep){
				tmpMonitorSetForOneTimeStep.add(monitor);
			}else{
				timeSteps.add(new ArrayList<Monitor>(tmpMonitorSetForOneTimeStep));
				tmpMonitorSetForOneTimeStep.clear();
				tmpMonitorSetForOneTimeStep.add(monitor);
				currentTimeStep++;	
				}
		}
		return timeSteps;	
	}
	

	//on input we have arrays of timesteps for all monitors.
	//i.e we have one array with number of monitors. and embed array of time intervals for each monitor.
	public static LinkedHashMap<Integer, Monitor> ttlRttDstPerMonitorsQueues(ArrayList<LinkedHashMap<Integer,Monitor>> queues){
																   
		// ArrayList<ArrayList<ArrayList<String>>> Monitors =  new ArrayList<ArrayList<ArrayList<String>>>();
		LinkedHashMap<Integer, Monitor> listMonitors = new LinkedHashMap<Integer, Monitor>();
		int cntMonitor = 0;
		for (LinkedHashMap<Integer,Monitor> monitor : queues){
			double sumTTL = 0;
			double sumRTT = 0.0;
			int cntElemInArr = 0;
			ArrayList<Integer> arrTTL = new ArrayList<Integer>();
			ArrayList<Double> arrRTT = new ArrayList<Double>();
			
			for ( Entry<Integer, Monitor> entry : monitor.entrySet()){
				 Monitor timeStep = entry.getValue();
				 sumRTT += timeStep.getRTT();
				 sumTTL += (double)timeStep.getTTL();
				 arrTTL.add(timeStep.getTTL());
				 arrRTT.add(timeStep.getRTT());
				 cntElemInArr++;
			}
			
			//possible error according to the counter that increments in the func listMonitors
			listMonitors.put(cntMonitor++, 
					new Monitor(sumRTT / cntElemInArr, 
								sumTTL / cntElemInArr,
								Calc.getStandartDeviation(Modify.arrDoubleToString(arrRTT)),
								Calc.getStandartDeviation(Modify.arrIntToString(arrTTL))
								));
			
		}
		return listMonitors; 
	}
	
	 
//	public static ArrayList<HashMap<Integer,Monitor>> destCompareTTLRTT (ArrayList<HashMap<Integer,Monitor>> listOfNotifyChanges){
	public static int[] destCompareTTLRTT (ArrayList<HashMap<Integer,Monitor>> listOfNotifyChanges){
		int[] cntArr = new int[1];
		int numSuccess = 0;
		int numFalse = 0;
		
		for (HashMap<Integer,Monitor> monitor:listOfNotifyChanges){
			for (int i=0;i<monitor.size();i++){
				if ((monitor.get(i).getTTL() == 1) &&
					(monitor.get(i).getRTT() == 1) &&
					 monitor.get(i).getDESTINATION().equals("1")){
					//monitor.get(i).setSuccess(1);
					 numSuccess++;
				}else{
					if(((	monitor.get(i).getTTL() == 1) &&
						(	monitor.get(i).getRTT() == 1) &&
							monitor.get(i).getDESTINATION().equals("0")) ||
						((	monitor.get(i).getTTL() == 0) &&
						(	monitor.get(i).getRTT() == 0) &&
						 	monitor.get(i).getDESTINATION().equals("1"))	
						){
							numFalse++;
						}
					
				}
			}
		}
		cntArr[0] = numFalse;
		cntArr[1] = numSuccess;
		return cntArr;
//		return listOfNotifyChanges;
	}
	
	public static ArrayList<Integer> TTLPerMonitor(Monitor desireMonitor, ArrayList<ArrayList<Monitor>> timeSteps){
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for ( ArrayList<Monitor> monitorsPerTimeStep : timeSteps){
			boolean matched = false;
			for( Monitor monitor :  monitorsPerTimeStep){
			 if ( monitor.getHOST().equals(desireMonitor.getHOST()) ){
				 arr.add(monitor.getTTL());
				 matched = true;
			 }
			}
			if (matched == false){
				 arr.add(0);
			}
		}
				
		return arr;
	}
	
	public static ArrayList<Double> RTTPerMonitor(Monitor desireMonitor, ArrayList<ArrayList<Monitor>> timeSteps){
		ArrayList<Double> arr = new ArrayList<Double>();
		for ( ArrayList<Monitor> monitorsPerTimeStep : timeSteps){
			boolean matched = false;
			for( Monitor monitor : monitorsPerTimeStep){
			 if ( monitor.getHOST().equals(desireMonitor.getHOST()) ){
				 arr.add(monitor.getRTT());
				 matched = true;
			 }
			}
			if (matched == false){
				 arr.add(0.0);
			}
		}
				
		return arr;
	}
	
	public static ArrayList<String> DESTPerMonitor(Monitor desireMonitor, ArrayList<ArrayList<Monitor>> timeSteps){
		ArrayList<String> arr = new ArrayList<String>();
		for ( ArrayList<Monitor> monitorsPerTimeStep : timeSteps){
			boolean matched = false;
			for( Monitor monitor : monitorsPerTimeStep){
			 if ( monitor.getHOST().equals(desireMonitor.getHOST()) ){
				 arr.add(monitor.getDESTINATION());
				 matched = true;
			 }
			}
			if (matched == false){
				 arr.add("");
			}
		}
				
		return arr;
	}
	
	public static ArrayList<LinkedHashMap<Integer,Monitor>> timeStepForHashMap(ArrayList<ArrayList<Monitor>> timeSteps){
		ArrayList<LinkedHashMap<Integer,Monitor>> timeStepHashMap = new ArrayList<LinkedHashMap<Integer,Monitor>>();
		LinkedHashMap<Integer,Monitor> hashMapOfOneTimeStep = new LinkedHashMap<Integer,Monitor>();
		int counter = 0;
		for (ArrayList<Monitor> monitorsPerTimeStep : timeSteps){
			timeStepHashMap.add(new LinkedHashMap<Integer,Monitor>(hashMapOfOneTimeStep));
			for (Monitor monitor: monitorsPerTimeStep){
				hashMapOfOneTimeStep.put(counter,monitor);
			}
			counter ++;
		}
	return timeStepHashMap;
	}
	
}

