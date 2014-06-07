/**
 * 
 */
package org.joogie.soot;

import java.util.LinkedList;
import java.util.List;

import org.joogie.util.TranslationHelpers;

import soot.SootField;
import soot.SootMethod;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
import util.Log;

/**
 * @author schaef
 *
 */
public class SootAnnotations {

	public enum Annotation {
		NonNull;
	}
	

	public static LinkedList<SootAnnotations.Annotation> parseFieldTags(SootField sf) {
		List<Tag> tags = sf.getTags();
		LinkedList<SootAnnotations.Annotation> annot = null;
		for (Tag t : tags) {
			if (t instanceof VisibilityAnnotationTag) {
				if (annot!=null) {
					throw new RuntimeException("Bug in parseFieldTags");
				}
				annot = parseAnnotations((VisibilityAnnotationTag)t);
			} else {
				Log.error("Unimplemented Tag found: "+t);
			}
		}
		if (annot == null) annot = new LinkedList<SootAnnotations.Annotation>();
		return annot;
	}
	
	public static LinkedList<SootAnnotations.Annotation> parseAnnotations(VisibilityAnnotationTag vtag) {
		LinkedList<SootAnnotations.Annotation> annot = new LinkedList<SootAnnotations.Annotation>();
		if (vtag == null || vtag.getAnnotations()==null) {
			//no annotation
			return annot;
		}
		for (AnnotationTag at : vtag.getAnnotations()) {
			addTagToList(annot, at);
		}
		return annot;
	}
	
	public static LinkedList<LinkedList<SootAnnotations.Annotation>> parseParameterAnnotations(SootMethod m) {
		
		LinkedList<LinkedList<SootAnnotations.Annotation>> pannot = new LinkedList<LinkedList<SootAnnotations.Annotation>>();
		
		for (Tag t : m.getTags()) {
			if (t instanceof VisibilityParameterAnnotationTag) {
				VisibilityParameterAnnotationTag tag = (VisibilityParameterAnnotationTag)t;
				if (tag.getVisibilityAnnotations().size() != m.getParameterCount()) {
					throw new RuntimeException("number of tags does not match number of params ... I did not understand this part!");
				}				
				for (VisibilityAnnotationTag va : tag.getVisibilityAnnotations()) {					
					pannot.add(parseAnnotations(va));
				}
			} 
		}
		return pannot;

	}
	
	private static void addTagToList(LinkedList<SootAnnotations.Annotation> annot, AnnotationTag at) {
		if (at!=null) {
			if (at.getType().contains("Lorg/eclipse/jdt/annotation/NonNull")) {
				Log.info("@NonNull found" );
				annot.add(Annotation.NonNull);
			} else {
				Log.error("Unhandled Annotation "+at);
			}
		} else {
			//no annotation
		}		
	}
	
	
	public static boolean inHackedListOfMethodsThatReturnNonNullValues(SootMethod m) {
		if (m.getSignature().contains("<java.lang.")) {
			//Log.error(m.getSignature() + "   " + TranslationHelpers.getQualifiedName(m));
		}
		
		if (m.getSignature().contains("<java.lang.String")) {
			if (m.getSignature().contains("toUpperCase")) return true;
			if (m.getSignature().contains("substring")) return true;
			if (m.getSignature().contains("subSequence")) return true;
			if (m.getSignature().contains("trim")) return true;
			if (m.getSignature().contains("toString")) return true;
			if (m.getSignature().contains("replace")) return true;
		}
		
		//TODO: Hack
		if (m.getSignature().contains("toLowerCase")) return true;
		if (m.getSignature().contains("toString")) return true;
		
		return false;
	}
	
}
