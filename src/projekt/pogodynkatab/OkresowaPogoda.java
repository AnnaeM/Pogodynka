package projekt.pogodynkatab;


public class OkresowaPogoda
{
    public String name;
  //  public String abbreviation;
  //  public String region;
    public String resourceId;

    public OkresowaPogoda()
        {
            // TODO Auto-generated constructor stub
        }

  //  public OkresowaPogoda(String name, String abbreviation, String region, String resourceFilePath)
    public OkresowaPogoda(String name, String resourceFilePath)
        {
            this.name = name;
            this.resourceId = resourceFilePath;
            
          /*  this.abbreviation = abbreviation;
            this.region= region;
            this.resourceId = resourceFilePath;*/
        }

    @Override
    public String toString()
        {
            return this.name;
        }
}