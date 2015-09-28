package com.lc.platform.dao.jpa;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

public class AliasToEntityMapResultTransformer extends AliasedTupleSubsetResultTransformer {

	private static final long serialVersionUID = 1L;
	public static final AliasToEntityMapResultTransformer INSTANCE = new AliasToEntityMapResultTransformer();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Disallow instantiation of AliasToEntityMapResultTransformer.
	 */
	private AliasToEntityMapResultTransformer() {
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		if(tuple.length==1){
			return tuple[0];
		}
		if(aliases.length>0 && aliases[0].equals("0")){
			return tuple;
		}
		Map<String,Object> result = new HashMap<String,Object>(tuple.length);
		for ( int i=0; i<tuple.length; i++ ) {
			String alias = aliases[i];
			if ( alias!=null ) {
				Object value = tuple[i];
				if(value instanceof Timestamp){
					value = formatter.format(value);
				}
				result.put( alias, value );
			}
		}
		return result;
	}

	@Override
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return false;
	}

	/**
	 * Serialization hook for ensuring singleton uniqueing.
	 *
	 * @return The singleton instance : {@link #INSTANCE}
	 */
	private Object readResolve() {
		return INSTANCE;
	}
}
